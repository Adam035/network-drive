package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.GetStorageResourceUseCase;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OpenService {
    private final GetStorageResourceUseCase getStorageResourceUseCase;

    private final OpenFileStateRegistry openFileStateRegistry;

    private final FileInfoMapper fileInfoMapper;

    public Optional<FileInfo> findFileInfo(String path, String volumeLabel) throws NTStatusException {
        Optional<OpenFileState> cachedState = openFileStateRegistry.findByPath(path);

        if (cachedState.isPresent()) {
            return Optional.of(fileInfoMapper.toFileInfo(cachedState.get()));
        }

        return findStorageResource(path, volumeLabel).map(fileInfoMapper::toFileInfo);
    }

    public Optional<StorageResource> findStorageResource(String path, String volumeLabel) throws NTStatusException {
        Optional<OpenFileState> cachedState = openFileStateRegistry.findByPath(path);
        if (cachedState.isPresent()) {
            return Optional.of(cachedState.get().getStorageResource());
        }
        try {
            return getStorageResourceUseCase.getStorageResource(path, volumeLabel);
        } catch (HttpClientErrorException e) {
            throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
        }
    }

    public FileInfo getFileInfo(long handle) {
        return fileInfoMapper.toFileInfo(openFileStateRegistry.require(handle));
    }

    public OpenResult open(
            long handle,
            String path,
            Set<CreateOptions> options,
            String volumeLabel
    ) throws NTStatusException {
        OpenFileState openFileState = openFileStateRegistry.require(handle);

        if (openFileState == null) {
            StorageResource resource = findStorageResource(path, volumeLabel)
                    .orElseThrow(() -> new NTStatusException(0xC0000034)); // STATUS_OBJECT_NAME_NOT_FOUND
            openFileState = new OpenFileState(path, resource);
            openFileStateRegistry.register(handle, openFileState);
        }


        return new OpenResult(handle, fileInfoMapper.toFileInfo(openFileState));
    }
}
