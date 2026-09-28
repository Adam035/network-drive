package io.github.adam035.desktopfs.infrastructure.winfsp.registry;

import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class FileHandleRegistry {

    private final AtomicLong handles;

    @Getter
    private final Map<Long, OpenFileState> openFileStates;

    public FileHandleRegistry() {
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

}
