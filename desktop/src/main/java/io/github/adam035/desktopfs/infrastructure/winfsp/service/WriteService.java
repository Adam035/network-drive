package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.FileHandleRegistry;
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

    private final FileHandleRegistry fileHandleRegistry;
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
        OpenFileState openFileState = fileHandleRegistry.require(ctx.getFileHandle());

        synchronized (openFileState) {
            FileInfo fileInfo = openFileState.getFileInfo();

            if (writeToEndOfFile && !constrainedIo) {
                offset = fileInfo.getFileSize();
            }

            int bytesToWrite = constrainedIo ? Math.clamp(fileInfo.getFileSize() - offset, 0, length) : length;

            if (bytesToWrite == 0) {
                return new WriteResult(0, fileInfo);
            }

            try {
                long endOffset = Math.addExact(offset, bytesToWrite);
                long newFileSize = Math.max(fileInfo.getFileSize(), endOffset);
                long newAllocationSize = allocationSize(newFileSize);

                if (openFileState.getTemporaryFile() == null) {
                    fileContentService.ensureLoaded(openFileState, volumeLabel);
                }

                byte[] bytes = new byte[bytesToWrite];
                buffer.get(0, bytes, 0, bytesToWrite);

                openFileState.setSynchronized(false);
                temporaryFileService.write(openFileState, offset, bytes);

                fileInfo.setFileSize(newFileSize);
                fileInfo.setAllocationSize(Math.max(fileInfo.getAllocationSize(), newAllocationSize));

                return new WriteResult(bytesToWrite, fileInfo);
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
        OpenFileState openFileState = fileHandleRegistry.require(openContext.getFileHandle());

        synchronized (openFileState) {
            FileInfo info = openFileState.getFileInfo();

            try {
                long newAllocationSize = allocationSize(newSize);
                boolean contentChanges = setAllocationSize
                        ? newSize < info.getFileSize()
                        : newSize != info.getFileSize();

                if (contentChanges) {
                    if (openFileState.getTemporaryFile() == null) {
                        fileContentService.ensureLoaded(openFileState, volumeLabel);
                    }

                    openFileState.setSynchronized(false);
                    temporaryFileService.setLength(openFileState, newSize);
                    info.setFileSize(newSize);
                }

                info.setAllocationSize(setAllocationSize
                        ? newAllocationSize
                        : Math.max(info.getAllocationSize(), newAllocationSize));

                return info;
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
