package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.DownloadFileUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.OpenHandleRegistry;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.TemporaryFileRegistry;
import jnr.ffi.Pointer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadService {
    private final OpenHandleRegistry openHandleRegistry;
    private final TemporaryFileRegistry temporaryFileRegistry;
    private final DownloadFileUseCase downloadFileUseCase;

    public long read(OpenContext ctx, Pointer buffer, long offset, int length, String volumeLabel)
            throws NTStatusException {
        OpenFileState state = openHandleRegistry.require(ctx.getFileHandle());
        synchronized (state) {
            if (offset < 0 || length < 0 || state.isDirectory()) {
                throw new NTStatusException(0xC000000D);
            }
            long size = state.getFileInfo().getFileSize();
            if (length == 0 || offset >= size) {
                return 0;
            }
            int count = (int) Math.min(length, size - offset);
            try {
                byte[] bytes = state.isLocalContentLoaded()
                        ? temporaryFileRegistry.read(state, offset, count)
                        : downloadFileUseCase.downloadFile(state.getPath(), offset, count, state.getVolumeLabel());
                if (bytes == null) {
                    throw new IOException("Backend returned no data");
                }
                int read = Math.min(count, bytes.length);
                if (read > 0) {
                    buffer.put(0, bytes, 0, read);
                }
                return read;
            } catch (IOException | RuntimeException e) {
                log.error("Cannot read {} at {}", state.getPath(), offset, e);
                throw new NTStatusException(0xC0000185);
            }
        }
    }
}
