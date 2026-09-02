package io.github.adam035.desktopfs.infrastructure.winfsp.registry;

import com.github.jnrwinfspteam.jnrwinfsp.api.FileInfo;
import com.github.jnrwinfspteam.jnrwinfsp.api.NTStatusException;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OpenHandleRegistry {
    private final AtomicLong handles = new AtomicLong();
    private final Map<Long, OpenFileState> openFiles = new HashMap<>();
    private final Map<ResourceKey, OpenFileState> resources = new HashMap<>();

    public long nextHandle() {
        return handles.incrementAndGet();
    }

    public synchronized Optional<OpenFileState> find(String volume, String path) {
        return Optional.ofNullable(resources.get(new ResourceKey(volume, normalize(path))));
    }

    public synchronized void register(long handle, OpenFileState state) {
        if (openFiles.containsKey(handle)) {
            throw new IllegalStateException("Duplicate handle: " + handle);
        }
        resources.put(new ResourceKey(state.getVolumeLabel(), state.getPath()), state);
        openFiles.put(handle, state);
        state.setOpenHandleCount(state.getOpenHandleCount() + 1);
    }

    public synchronized OpenFileState require(long handle) throws NTStatusException {
        OpenFileState state = openFiles.get(handle);
        if (state == null) {
            throw new NTStatusException(0xC0000008); // STATUS_INVALID_HANDLE
        }
        return state;
    }

    public String getPathByFileHandle(long handle) throws NTStatusException {
        return require(handle).getPath();
    }

    public FileInfo getFileInfoByFileHandle(long handle) throws NTStatusException {
        return require(handle).getFileInfo();
    }

    public synchronized OpenFileState release(long handle) {
        OpenFileState state = openFiles.remove(handle);
        if (state != null) {
            state.setOpenHandleCount(state.getOpenHandleCount() - 1);
        }
        return state;
    }

    public synchronized void forget(OpenFileState state) {
        resources.remove(new ResourceKey(state.getVolumeLabel(), state.getPath()), state);
    }

    public synchronized List<OpenFileState> states(String volume) {
        return resources.values().stream()
                .filter(state -> state.getVolumeLabel().equals(volume))
                .toList();
    }

    public synchronized void markDeleted(OpenFileState state) {
        for (OpenFileState candidate : states(state.getVolumeLabel())) {
            if (candidate == state || (state.isDirectory()
                    && candidate.getPath().startsWith(state.getPath() + "\\"))) {
                forget(candidate);
                candidate.setDeleted(true);
                candidate.setDirty(false);
            }
        }
    }

    public synchronized void rename(OpenFileState source, String newPath) {
        String oldPath = source.getPath();
        List<OpenFileState> affected = states(source.getVolumeLabel()).stream()
                .filter(state -> state == source || (source.isDirectory()
                        && state.getPath().startsWith(oldPath + "\\")))
                .toList();
        for (OpenFileState state : affected) {
            forget(state);
            String path = newPath + state.getPath().substring(oldPath.length());
            state.setPath(path);
            FileInfo previous = state.getFileInfo();
            FileInfo renamed = new FileInfo(path.substring(path.lastIndexOf('\\') + 1));
            renamed.setNormalizedName(path);
            renamed.getFileAttributes().addAll(previous.getFileAttributes());
            renamed.setFileSize(previous.getFileSize());
            renamed.setAllocationSize(previous.getAllocationSize());
            renamed.setCreationTime(previous.getCreationTime());
            renamed.setLastAccessTime(previous.getLastAccessTime());
            renamed.setLastWriteTime(previous.getLastWriteTime());
            renamed.setChangeTime(previous.getChangeTime());
            state.setFileInfo(renamed);
            resources.put(new ResourceKey(state.getVolumeLabel(), path), state);
        }
    }

    public static String normalize(String path) {
        if (path == null || path.isBlank()) {
            return "\\";
        }
        String normalized = path.replace('/', '\\');
        while (normalized.contains("\\\\")) {
            normalized = normalized.replace("\\\\", "\\");
        }
        if (!normalized.startsWith("\\")) {
            normalized = "\\" + normalized;
        }
        while (normalized.length() > 1 && normalized.endsWith("\\")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    public static String childPath(String parent, String name) {
        return normalize(parent + "\\" + name);
    }

    private record ResourceKey(String volume, String path) {}
}
