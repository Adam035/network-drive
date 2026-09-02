package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlushService {
    private final OpenHandleRegistry openHandleRegistry;
    private final FileSynchronizationService synchronizationService;

    public FileInfo flush(OpenContext ctx, String volumeLabel) throws NTStatusException {
        if (ctx == null) {
            for (OpenFileState state : openHandleRegistry.states(volumeLabel)) {
                synchronizationService.synchronize(state);
            }
            return null;
        }
        OpenFileState state = openHandleRegistry.require(ctx.getFileHandle());
        synchronizationService.synchronize(state);
        return state.getFileInfo();
    }
}
