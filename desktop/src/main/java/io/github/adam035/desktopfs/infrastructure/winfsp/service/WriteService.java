package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.TemporaryFileRegistry;
import jnr.ffi.Pointer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class WriteService {
    private final OpenHandleRegistry openHandleRegistry;
    private final TemporaryFileRegistry temporaryFileRegistry;

    public WriteResult write(OpenContext ctx, Pointer buffer, long offset, int length,
                             boolean writeToEndOfFile, boolean constrainedIo) throws NTStatusException {
        OpenFileState state = openHandleRegistry.require(ctx.getFileHandle());
        synchronized (state) {
            FileInfo info = state.getFileInfo();
            // For constrained I/O, the supplied offset is authoritative.
            if (writeToEndOfFile && !constrainedIo) {
                offset = info.getFileSize();
            }
            if (offset < 0 || length < 0 || state.isDirectory() || state.isDeleted()) {
                throw new NTStatusException(0xC000000D); // INVALID_PARAMETER
            }
            int count = constrainedIo
                    ? (int) Math.min(length, Math.max(0, info.getFileSize() - offset))
                    : length;
            if (count == 0) {
                return new WriteResult(0, info);
            }
            try {
                long end = Math.addExact(offset, count);
                long size = Math.max(info.getFileSize(), end);
                long allocation = OpenFileState.allocationSize(size);
                temporaryFileRegistry.ensureLoaded(state);
                RandomAccessFile file = state.getTemporaryFile();
                byte[] bytes = new byte[count];
                buffer.get(0, bytes, 0, count);
                file.seek(offset);
                // Preserve a dirty copy even if the physical write fails partway through.
                state.setDirty(true);
                file.write(bytes);
                info.setFileSize(size);
                info.setAllocationSize(Math.max(info.getAllocationSize(), allocation));
                return new WriteResult(count, info);
            } catch (IOException | RuntimeException e) {
                log.error("Cannot write {} at {}", state.getPath(), offset, e);
                throw new NTStatusException(0xC0000185);
            }
        }
    }

    public FileInfo setFileSize(OpenContext ctx, long newSize, boolean setAllocationSize)
            throws NTStatusException {
        OpenFileState state = openHandleRegistry.require(ctx.getFileHandle());
        synchronized (state) {
            if (newSize < 0 || state.isDirectory() || state.isDeleted()) {
                throw new NTStatusException(0xC000000D);
            }
            FileInfo info = state.getFileInfo();
            try {
                long allocation = OpenFileState.allocationSize(newSize);
                boolean changesContent = !setAllocationSize
                        ? newSize != info.getFileSize() : newSize < info.getFileSize();
                if (changesContent) {
                    temporaryFileRegistry.ensureLoaded(state);
                    state.setDirty(true);
                    state.getTemporaryFile().setLength(newSize);
                    info.setFileSize(newSize);
                }
                info.setAllocationSize(setAllocationSize
                        ? allocation : Math.max(info.getAllocationSize(), allocation));
                return info;
            } catch (IOException | RuntimeException e) {
                log.error("Cannot resize {}", state.getPath(), e);
                throw new NTStatusException(0xC0000185);
            }
        }
    }
}
