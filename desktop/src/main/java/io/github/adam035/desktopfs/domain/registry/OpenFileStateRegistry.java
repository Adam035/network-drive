package io.github.adam035.desktopfs.domain.registry;

import io.github.adam035.desktopfs.domain.model.OpenFileState;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OpenFileStateRegistry {

    private final AtomicLong handles;

    @Getter
    private final Map<Long, OpenFileState> openFileStates;

    public OpenFileStateRegistry() {
        handles = new AtomicLong();
        openFileStates = new HashMap<>();
    }

    public long nextHandle() {
        return handles.incrementAndGet();
    }

    public synchronized void register(long handle, OpenFileState state) {
        OpenFileState previousOpenFileState = openFileStates.putIfAbsent(handle, state);

        if (previousOpenFileState != null) {
            throw new IllegalStateException("Handle is already registered: ".concat(String.valueOf(handle)));
        }
    }

    public synchronized OpenFileState require(long handle) {
        return openFileStates.get(handle);
    }

    public synchronized OpenFileState release(long handle) {
        return openFileStates.remove(handle);
    }

    public synchronized boolean contains(OpenFileState openFileState) {
        return openFileStates.containsValue(openFileState);
    }

    public synchronized Optional<OpenFileState> findByPath(String path) {
        return openFileStates.values().stream()
                .filter(state -> state.getPath().equals(path))
                .findFirst();
    }

    public synchronized void renamePaths(String oldPath, String newPath) {
        String prefix = oldPath.endsWith("\\") ? oldPath : oldPath.concat("\\");
        for (OpenFileState state : openFileStates.values()) {
            String path = state.getPath();

            if (path.equalsIgnoreCase(oldPath) || path.regionMatches(true, 0, prefix, 0, prefix.length())) {
                String renamedPath = newPath + path.substring(oldPath.length());
                state.setPath(renamedPath);

                if (path.equalsIgnoreCase(oldPath)) {
                    state.getStorageResource().setName(newPath.substring(newPath.lastIndexOf('\\') + 1));
                }
            }
        }
    }

}
