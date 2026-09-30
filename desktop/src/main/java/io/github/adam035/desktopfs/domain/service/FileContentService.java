package io.github.adam035.desktopfs.domain.service;

import io.github.adam035.desktopfs.application.usecase.DownloadFileUseCase;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileContentService {
    private static final int DOWNLOAD_CHUNK_SIZE = 1024 * 1024;

    private final DownloadFileUseCase downloadFileUseCase;

    private final TemporaryFileService temporaryFileService;

    public void ensureLoaded(OpenFileState openFileState, String volumeLabel) throws IOException {
        temporaryFileService.setLength(openFileState, 0);

        long expectedSize = openFileState.getStorageResource().getSize();
        long offset = 0;

        while (offset < expectedSize) {
            int length = (int) Math.min(DOWNLOAD_CHUNK_SIZE, expectedSize - offset);
            byte[] bytes = downloadFileUseCase.downloadFile(openFileState.getPath(), offset, length, volumeLabel);

            if (bytes == null || bytes.length == 0 || bytes.length > length) {
                throw new IOException("Incomplete download of " + openFileState.getPath());
            }

            temporaryFileService.write(openFileState, offset, bytes);
            offset += bytes.length;
        }
    }

}
