package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
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
public class OverwriteService {
    private final FileHandleRegistry fileHandleRegistry;
    private final TemporaryFileService temporaryFileService;

    public FileInfo overwrite(
            OpenContext ctx,
            Set<FileAttributes> attributes,
            boolean replaceAttributes,
            long allocationSize
    ) throws NTStatusException {
        OpenFileState openFileState = fileHandleRegistry.require(ctx.getFileHandle());

        try {
            temporaryFileService.truncate(openFileState);
            openFileState.setSynchronized(false);

            FileInfo fileInfo = openFileState.getFileInfo();
            fileInfo.setFileSize(0);
            fileInfo.setAllocationSize(allocationSize);

            if (replaceAttributes) {
                fileInfo.getFileAttributes().clear();
            }

            if (attributes != null) {
                fileInfo.getFileAttributes().addAll(attributes);
            }

            fileInfo.getFileAttributes().remove(FileAttributes.FILE_ATTRIBUTE_DIRECTORY);

            if (fileInfo.getFileAttributes().isEmpty()) {
                fileInfo.getFileAttributes().add(FileAttributes.FILE_ATTRIBUTE_NORMAL);
            } else if (fileInfo.getFileAttributes().size() > 1) {
                fileInfo.getFileAttributes().remove(FileAttributes.FILE_ATTRIBUTE_NORMAL);
            }

            return fileInfo;
        } catch (IOException e) {
            log.error("Cannot overwrite {}", openFileState.getPath(), e);
            throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
        }
    }

}
