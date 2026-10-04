package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.MoveStorageResourceUseCase;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RenameService {

    private final OpenFileStateRegistry openFileStateRegistry;

    private final MoveStorageResourceUseCase moveStorageResourceUseCase;

    public void rename(
            OpenContext openContext,
            String oldPath,
            String newPath,
            boolean replaceIfExists,
            String volumeLabel
    ) throws NTStatusException {
        if (oldPath.equals(newPath)) {
            return;
        }

        try {
            moveStorageResourceUseCase.moveStorageResource(oldPath, newPath, replaceIfExists, volumeLabel);
            openFileStateRegistry.renamePaths(oldPath, newPath);
        } catch (RuntimeException e) {
            log.error("Cannot rename {} to {}", oldPath, newPath, e);
            throw new NTStatusException(0xC0000185);
        }
    }

}
