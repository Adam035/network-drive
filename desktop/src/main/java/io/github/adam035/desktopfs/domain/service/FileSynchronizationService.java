package io.github.adam035.desktopfs.domain.service;

import io.github.adam035.desktopfs.application.usecase.UploadFileUseCase;
import io.github.adam035.desktopfs.domain.model.OpenFileState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileSynchronizationService {

    private final TemporaryFileService temporaryFileService;

    private final UploadFileUseCase uploadFileUseCase;

    public void synchronize(OpenFileState openFileState, String volumeLabel) throws IOException {
        if (openFileState.isSynchronized()) {
            return;
        }

        long size = temporaryFileService.length(openFileState);
        if (size != openFileState.getStorageResource().getSize()) {
            throw new IOException("Local size does not match metadata");
        }

        byte[] bytes = temporaryFileService.read(openFileState, 0, (int) size);

        if (bytes.length != size) {
            throw new IOException("Incomplete local read");
        }

        uploadFileUseCase.uploadFile(openFileState.getPath(), bytes, volumeLabel);
        openFileState.setSynchronized(true);
    }
}
