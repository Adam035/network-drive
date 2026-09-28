package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.GetStorageResourceUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.FileHandleRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OpenService {
    private final GetStorageResourceUseCase getStorageResourceUseCase;
    private final FileHandleRegistry fileHandleRegistry;
    private final FileInfoMapper fileInfoMapper;

    public Optional<FileInfo> findFileInfo(String path, String volumeLabel) throws NTStatusException {
        Optional<OpenFileState> cachedState = fileHandleRegistry.findByPath(path);

        if (cachedState.isPresent()) {
            return Optional.of(cachedState.get().getFileInfo());
        }

        try {
            return getStorageResourceUseCase.getStorageResource(path, volumeLabel)
                    .map(fileInfoMapper::toFileInfo);
        } catch (HttpClientErrorException e) {
            throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
        }
    }

    public OpenResult open(
            long handle,
            String path,
            Set<CreateOptions> options,
            String volumeLabel
    ) throws NTStatusException {
        OpenFileState openFileState = fileHandleRegistry.require(handle);

        if (openFileState == null) {
            FileInfo fileInfo = findFileInfo(path, volumeLabel)
                    .orElseThrow(() -> new NTStatusException(0xC0000034)); // STATUS_OBJECT_NAME_NOT_FOUND
            openFileState = new OpenFileState(path, fileInfo);
            fileHandleRegistry.register(handle, openFileState);
        }


        return new OpenResult(handle, openFileState.getFileInfo());
    }
}
