package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.DeleteStorageResourceUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.FileHandleRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CleanupService {

    private final FileHandleRegistry fileHandleRegistry;

    private final DeleteStorageResourceUseCase deleteStorageResourceUseCase;

    private final FileSynchronizationService synchronizationService;

    public void cleanup(OpenContext ctx, Set<CleanupFlags> flags, String volumeLabel) {
        try {
            OpenFileState openFileState = fileHandleRegistry.require(ctx.getFileHandle());

            if (flags.contains(CleanupFlags.DELETE)) {
                deleteStorageResourceUseCase.deleteStorageResource(openFileState.getPath(), volumeLabel);
            }

            synchronizationService.synchronize(openFileState, volumeLabel);
        } catch (IOException e) {
            log.error("Cannot cleanup {}", ctx.getFileHandle(), e);
        }
    }
}
