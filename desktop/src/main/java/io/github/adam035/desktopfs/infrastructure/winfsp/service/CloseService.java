package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.OpenContext;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.TemporaryFileRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CloseService {
    private final OpenHandleRegistry openHandleRegistry;
    private final TemporaryFileRegistry temporaryFileRegistry;

    public void close(OpenContext ctx) {
        OpenFileState state = openHandleRegistry.release(ctx.getFileHandle());

        if (state == null || state.getOpenHandleCount() != 0) {
            return;
        }

        boolean discard = state.isDeleted() || !state.isDirty();
        try {
            temporaryFileRegistry.close(state, discard);
            if (discard) {
                openHandleRegistry.forget(state);
            } else {
                log.error("Unsynchronized file retained: {} -> {}", state.getPath(), state.getTemporaryPath());
            }
        } catch (IOException e) {
            log.error("Cannot close staging file {}", state.getTemporaryPath(), e);
        }
    }
}
