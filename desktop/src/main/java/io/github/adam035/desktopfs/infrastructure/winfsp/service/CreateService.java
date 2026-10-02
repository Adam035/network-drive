package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.CreateDirectoryUseCase;
import io.github.adam035.desktopfs.application.usecase.UploadFileUseCase;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.github.jnrwinfspteam.jnrwinfsp.api.CreateOptions.FILE_DIRECTORY_FILE;
import static com.github.jnrwinfspteam.jnrwinfsp.api.FileAttributes.FILE_ATTRIBUTE_DIRECTORY;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateService {

    private final CreateDirectoryUseCase createDirectoryUseCase;

    private final UploadFileUseCase uploadFileUseCase;

    private final OpenService openService;

    private final OpenFileStateRegistry openFileStateRegistry;

    private final FileInfoMapper fileInfoMapper;

    public OpenResult create(
            String path,
            Set<CreateOptions> options,
            long handle,
            String volumeLabel,
            Set<FileAttributes> attributes,
            byte[] securityDescriptor,
            long allocationSize
    ) throws NTStatusException {
        if (allocationSize < 0 || allocationSize > Long.MAX_VALUE - 511) {
            throw new NTStatusException(0xC000000D); // INVALID_PARAMETER
        }

        if (openService.findFileInfo(path, volumeLabel).isPresent()) {
            throw new NTStatusException(0xC0000035); // NAME_COLLISION
        }

        String parentPath = path.substring(0, path.lastIndexOf('\\'));
        FileInfo parentInfo = openService.findFileInfo(parentPath, volumeLabel)
                .orElseThrow(() -> new NTStatusException(0xC000003A)); // PATH_NOT_FOUND

        if (!parentInfo.getFileAttributes().contains(FILE_ATTRIBUTE_DIRECTORY)) {
            throw new NTStatusException(0xC0000103); // STATUS_NOT_A_DIRECTORY
        }

        StorageResource resource = options.contains(FILE_DIRECTORY_FILE)
                ? createDirectory(path, volumeLabel)
                : createFile(path, volumeLabel);

        OpenFileState state = new OpenFileState(path, resource);
        if (resource.getType() == StorageResource.Type.FILE) {
            state.setAllocationSize(allocationSize);
        }
        openFileStateRegistry.register(handle, state);
        return new OpenResult(handle, fileInfoMapper.toFileInfo(state));
    }

    private StorageResource createDirectory(String path, String volumeLabel) {
        return createDirectoryUseCase.createDirectory(path, volumeLabel);
    }

    private StorageResource createFile(String path, String volumeLabel) throws NTStatusException {
        uploadFileUseCase.uploadFile(path, new byte[0], volumeLabel);

        return openService.findStorageResource(path, volumeLabel)
                .orElseThrow(() -> new NTStatusException(0xC0000034)); // STATUS_OBJECT_NAME_NOT_FOUND
    }

}
