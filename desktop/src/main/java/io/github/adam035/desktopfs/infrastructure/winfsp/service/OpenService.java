package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.GetStorageResourceUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OpenService {
    private final GetStorageResourceUseCase getStorageResourceUseCase;
    private final OpenHandleRegistry openHandleRegistry;
    private final FileInfoMapper fileInfoMapper;

    public Optional<FileInfo> findInfo(String path, String volumeLabel) throws NTStatusException {
        String normalized = OpenHandleRegistry.normalize(path);
        Optional<OpenFileState> local = openHandleRegistry.find(volumeLabel, normalized);
        if (local.isPresent()) {
            return Optional.of(local.get().getFileInfo());
        }
        try {
            return getStorageResourceUseCase.getStorageResource(normalized, volumeLabel)
                    .map(fileInfoMapper::toFileInfo);
        } catch (RuntimeException e) {
            throw new NTStatusException(0xC0000185);
        }
    }

    public OpenResult open(long handle, String path, Set<CreateOptions> options, String volumeLabel)
            throws NTStatusException {
        String normalized = OpenHandleRegistry.normalize(path);
        OpenFileState state = openHandleRegistry.find(volumeLabel, normalized).orElse(null);
        if (state == null) {
            FileInfo info = findInfo(normalized, volumeLabel)
                    .orElseThrow(() -> new NTStatusException(0xC0000034)); // NAME_NOT_FOUND
            boolean directory = info.getFileAttributes().contains(FileAttributes.FILE_ATTRIBUTE_DIRECTORY);
            state = new OpenFileState(volumeLabel, normalized, directory, info);
        }
        if (options.contains(CreateOptions.FILE_DIRECTORY_FILE) && !state.isDirectory()) {
            throw new NTStatusException(0xC0000103); // NOT_A_DIRECTORY
        }
        if (options.contains(CreateOptions.FILE_NON_DIRECTORY_FILE) && state.isDirectory()) {
            throw new NTStatusException(0xC00000BA); // FILE_IS_A_DIRECTORY
        }
        openHandleRegistry.register(handle, state);
        return new OpenResult(handle, state.getFileInfo());
    }
}
