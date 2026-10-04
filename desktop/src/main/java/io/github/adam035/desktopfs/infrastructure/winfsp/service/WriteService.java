package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import io.github.adam035.desktopfs.domain.service.FileContentService;
import io.github.adam035.desktopfs.domain.service.TemporaryFileService;
import io.github.adam035.desktopfs.infrastructure.winfsp.mapper.FileInfoMapper;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import jnr.ffi.Pointer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class WriteService {
    private static final long ALLOCATION_UNIT = 512;

    private final OpenFileStateRegistry openFileStateRegistry;

    private final FileInfoMapper fileInfoMapper;

    private final FileContentService fileContentService;

    private final TemporaryFileService temporaryFileService;

    public WriteResult write(
            OpenContext ctx,
            Pointer buffer,
            long offset,
            int length,
            boolean writeToEndOfFile,
            boolean constrainedIo,
            String volumeLabel
    ) throws NTStatusException {
        OpenFileState openFileState = openFileStateRegistry.require(ctx.getFileHandle());

        synchronized (openFileState) {
            var resource = openFileState.getStorageResource();

            if (writeToEndOfFile && !constrainedIo) {
                offset = resource.getSize();
            }

            int bytesToWrite = constrainedIo ? Math.clamp(resource.getSize() - offset, 0, length) : length;

            if (bytesToWrite == 0) {
                return new WriteResult(0, fileInfoMapper.toFileInfo(openFileState));
            }

            try {
                long endOffset = Math.addExact(offset, bytesToWrite);
                long newFileSize = Math.max(resource.getSize(), endOffset);
                long newAllocationSize = allocationSize(newFileSize);

                if (openFileState.getTemporaryFile() == null) {
                    fileContentService.ensureLoaded(openFileState, volumeLabel);
                }

                byte[] bytes = new byte[bytesToWrite];
                buffer.get(0, bytes, 0, bytesToWrite);

                openFileState.setSynchronized(false);
                temporaryFileService.write(openFileState, offset, bytes);

                resource.setSize(newFileSize);
                openFileState.setAllocationSize(Math.max(openFileState.getAllocationSize(), newAllocationSize));

                return new WriteResult(bytesToWrite, fileInfoMapper.toFileInfo(openFileState));
            } catch (IOException e) {
                log.error("Cannot write {} at {}", openFileState.getPath(), offset, e);
                throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
            }
        }
    }

    public FileInfo setFileSize(
            OpenContext openContext,
            long newSize,
            boolean setAllocationSize,
            String volumeLabel
    ) throws NTStatusException {
        OpenFileState openFileState = openFileStateRegistry.require(openContext.getFileHandle());

        synchronized (openFileState) {
            var resource = openFileState.getStorageResource();

            try {
                long newAllocationSize = allocationSize(newSize);
                boolean contentChanges = setAllocationSize
                        ? newSize < resource.getSize()
                        : newSize != resource.getSize();

                if (contentChanges) {
                    if (openFileState.getTemporaryFile() == null) {
                        fileContentService.ensureLoaded(openFileState, volumeLabel);
                    }

                    openFileState.setSynchronized(false);
                    temporaryFileService.setLength(openFileState, newSize);
                    resource.setSize(newSize);
                }

                openFileState.setAllocationSize(setAllocationSize
                        ? newAllocationSize
                        : Math.max(openFileState.getAllocationSize(), newAllocationSize));

                return fileInfoMapper.toFileInfo(openFileState);
            } catch (IOException | RuntimeException e) {
                log.error("Cannot resize {}", openFileState.getPath(), e);
                throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
            }
        }
    }

    private long allocationSize(long size) {
        if (size < 0 || size > Long.MAX_VALUE - (ALLOCATION_UNIT - 1)) {
            throw new ArithmeticException("Invalid file size: " + size);
        }

        return ((size + ALLOCATION_UNIT - 1) / ALLOCATION_UNIT) * ALLOCATION_UNIT;
    }
}
