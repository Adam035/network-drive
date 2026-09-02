package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.MoveStorageResourceUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RenameService {
    private final OpenHandleRegistry openHandleRegistry;
    private final MoveStorageResourceUseCase moveStorageResourceUseCase;
    private final FileSynchronizationService synchronizationService;
    private final OpenService openService;

    public void rename(OpenContext ctx, String oldPath, String newPath, boolean replaceIfExists,
                       String volumeLabel) throws NTStatusException {
        OpenFileState state = openHandleRegistry.require(ctx.getFileHandle());
        oldPath = state.getPath();
        newPath = OpenHandleRegistry.normalize(newPath);
        if (oldPath.equals(newPath)) {
            return;
        }
        if ("\\".equals(oldPath) || "\\".equals(newPath)
                || (state.isDirectory() && newPath.startsWith(oldPath + "\\"))) {
            throw new NTStatusException(0xC0000022);
        }
        for (OpenFileState target : openHandleRegistry.states(state.getVolumeLabel())) {
            if (target != state && (target.getPath().equals(newPath)
                    || target.getPath().startsWith(newPath + "\\"))) {
                throw new NTStatusException(0xC0000043); // SHARING_VIOLATION
            }
        }
        FileInfo targetInfo = openService.findInfo(newPath, volumeLabel).orElse(null);
        if (targetInfo != null && (!replaceIfExists || state.isDirectory()
                || targetInfo.getFileAttributes().contains(FileAttributes.FILE_ATTRIBUTE_DIRECTORY))) {
            throw new NTStatusException(0xC0000035);
        }
        // Commit under the old remote name before changing the namespace.
        for (OpenFileState candidate : openHandleRegistry.states(state.getVolumeLabel())) {
            if (candidate == state || (state.isDirectory()
                    && candidate.getPath().startsWith(oldPath + "\\"))) {
                synchronizationService.synchronize(candidate);
            }
        }
        try {
            moveStorageResourceUseCase.moveStorageResource(oldPath, newPath, replaceIfExists, state.getVolumeLabel());
        } catch (RuntimeException e) {
            log.error("Cannot rename {} to {}", oldPath, newPath, e);
            throw new NTStatusException(0xC0000185);
        }
        openHandleRegistry.rename(state, newPath);
    }
}
