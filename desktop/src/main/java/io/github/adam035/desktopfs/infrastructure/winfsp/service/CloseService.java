package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.OpenContext;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import io.github.adam035.desktopfs.domain.service.TemporaryFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CloseService {

    private final OpenFileStateRegistry openFileStateRegistry;

    private final TemporaryFileService temporaryFileService;

    public void close(OpenContext ctx) {
        OpenFileState openFileState = openFileStateRegistry.release(ctx.getFileHandle());

        if (openFileState == null) {
            return;
        }

        try {
            temporaryFileService.close(openFileState);
        } catch (IOException e) {
            log.error("Cannot close staging file {}", openFileState.getPath(), e);
        }
    }
}
