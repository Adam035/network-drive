package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import io.github.adam035.desktopfs.domain.service.TemporaryFileService;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class OverwriteService {
    private final OpenFileStateRegistry openFileStateRegistry;

    private final FileInfoMapper fileInfoMapper;

    private final TemporaryFileService temporaryFileService;

    public FileInfo overwrite(
            OpenContext ctx,
            Set<FileAttributes> attributes,
            boolean replaceAttributes,
            long allocationSize
    ) throws NTStatusException {
        OpenFileState openFileState = openFileStateRegistry.require(ctx.getFileHandle());

        try {
            temporaryFileService.truncate(openFileState);
            openFileState.setSynchronized(false);

            var resource = openFileState.getStorageResource();
            resource.setSize(0L);
            openFileState.setAllocationSize(allocationSize);

            return fileInfoMapper.toFileInfo(openFileState);
        } catch (IOException e) {
            log.error("Cannot overwrite {}", openFileState.getPath(), e);
            throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
        }
    }

}
