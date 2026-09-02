package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.CreateDirectoryUseCase;
import io.github.adam035.desktopfs.application.usecase.UploadFileUseCase;
import io.github.adam035.desktopfs.domain.model.File;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateService {

    private final CreateDirectoryUseCase createDirectoryUseCase;

    private final UploadFileUseCase uploadFileUseCase;

    private final OpenService openService;

    private final OpenHandleRegistry openHandleRegistry;

    private final FileInfoMapper fileInfoMapper;

    public OpenResult create(
            String path,
            Set<CreateOptions> options,
            long handle,
            String volume,
            Set<FileAttributes> attributes,
            byte[] securityDescriptor,
            long allocationSize
    ) throws NTStatusException {
        path = OpenHandleRegistry.normalize(path);

        if (allocationSize < 0 || allocationSize > Long.MAX_VALUE - 511) {
            throw new NTStatusException(0xC000000D); // INVALID_PARAMETER
        }

        if (openService.findInfo(path, volume).isPresent()) {
            throw new NTStatusException(0xC0000035); // NAME_COLLISION
        }

        String parent = path.substring(0, path.lastIndexOf('\\'));
        FileInfo parentInfo = openService.findInfo(parent, volume)
                .orElseThrow(() -> new NTStatusException(0xC000003A)); // PATH_NOT_FOUND

        if (!parentInfo.getFileAttributes().contains(FileAttributes.FILE_ATTRIBUTE_DIRECTORY)) {
            throw new NTStatusException(0xC0000103);
        }

        boolean directory = options.contains(CreateOptions.FILE_DIRECTORY_FILE);
        FileInfo info;
        try {
            if (directory) {
                info = fileInfoMapper.toFileInfo(createDirectoryUseCase.createDirectory(path, volume));
            } else {
                uploadFileUseCase.uploadFile(path, new byte[0], "application/octet-stream", volume);
                File file = new File();
                file.setName(path.substring(path.lastIndexOf('\\') + 1));
                file.setPath(path);
                file.setSize(0L);
                file.setCreatedAt(Instant.now());
                file.setUpdatedAt(file.getCreatedAt());
                info = fileInfoMapper.toFileInfo(file);
                info.setAllocationSize(OpenFileState.allocationSize(allocationSize));
            }
        } catch (RuntimeException e) {
            log.error("Cannot create {}", path, e);
            throw new NTStatusException(0xC0000185);
        }

        if (attributes != null) {
            info.getFileAttributes().addAll(attributes);
        }

        if (directory) {
            info.getFileAttributes().add(FileAttributes.FILE_ATTRIBUTE_DIRECTORY);
        } else {
            info.getFileAttributes().remove(FileAttributes.FILE_ATTRIBUTE_DIRECTORY);
        }

        if (info.getFileAttributes().size() > 1) {
            info.getFileAttributes().remove(FileAttributes.FILE_ATTRIBUTE_NORMAL);
        }

        OpenFileState state = new OpenFileState(volume, path, directory, info);
        state.setSecurityDescriptor(securityDescriptor == null ? null : securityDescriptor.clone());
        openHandleRegistry.register(handle, state);

        return new OpenResult(handle, info);
    }

}
