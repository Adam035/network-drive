package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.*;
import io.github.adam035.desktopfs.application.usecase.DownloadFileUseCase;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import io.github.adam035.desktopfs.domain.registry.OpenFileStateRegistry;
import io.github.adam035.desktopfs.domain.service.TemporaryFileService;
import jnr.ffi.Pointer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadService {

    private final OpenFileStateRegistry openFileStateRegistry;

    private final TemporaryFileService temporaryFileService;

    private final DownloadFileUseCase downloadFileUseCase;

    public long read(
            OpenContext openContext,
            Pointer buffer,
            long offset,
            int length,
            String volumeLabel
    ) throws NTStatusException {
        OpenFileState openFileState = openFileStateRegistry.require(openContext.getFileHandle());

        if (offset < 0 || length < 0) {
            throw new NTStatusException(0xC000000D);
        }

        long size = openFileState.getStorageResource().getSize();

        if (length == 0 || offset >= size) {
            return 0;
        }

        int count = (int) Math.min(length, size - offset);

        try {
            byte[] bytes = openFileState.getTemporaryFile() != null
                    ? temporaryFileService.read(openFileState, offset, count)
                    : downloadFileUseCase.downloadFile(openFileState.getPath(), offset, count, volumeLabel);

            if (bytes == null || bytes.length == 0) {
                throw new IOException("No data returned before end of file");
            }

            int readBytesLength = Math.min(count, bytes.length);

            buffer.put(0, bytes, 0, readBytesLength);

            return readBytesLength;
        } catch (IOException | RuntimeException e) {
            log.error("Cannot read {} at {}", openFileState.getPath(), offset, e);
            throw new NTStatusException(0xC0000185);
        }
    }
}
