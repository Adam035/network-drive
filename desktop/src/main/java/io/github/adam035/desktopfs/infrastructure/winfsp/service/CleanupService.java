package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.DeleteStorageResourceUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CleanupService {
    private final OpenHandleRegistry openHandleRegistry;
    private final DeleteStorageResourceUseCase deleteStorageResourceUseCase;
    private final FileSynchronizationService synchronizationService;

    public void cleanup(OpenContext ctx, Set<CleanupFlags> flags, String volumeLabel) {
        try {
            OpenFileState state = openHandleRegistry.require(ctx.getFileHandle());

            if (state.isDeleted()) {
                return;
            }

            if (flags.contains(CleanupFlags.DELETE)) {
                deleteStorageResourceUseCase.deleteStorageResource(state.getPath(), state.getVolumeLabel());
                openHandleRegistry.markDeleted(state);
                return;
            }

            synchronizationService.synchronize(state);
        } catch (NTStatusException | RuntimeException e) {
            log.error("Cannot cleanup {}", ctx.getFileHandle(), e);
        }
    }
}
