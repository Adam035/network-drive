package io.github.adam035.desktopfs.infrastructure.winfsp.service;

import com.github.jnrwinfspteam.jnrwinfsp.api.NTStatusException;
import io.github.adam035.desktopfs.application.usecase.UploadFileUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import io.github.adam035.desktopfs.infrastructure.winfsp.registry.TemporaryFileRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileSynchronizationService {
    private final TemporaryFileRegistry temporaryFileRegistry;
    private final UploadFileUseCase uploadFileUseCase;

    public void synchronize(OpenFileState state) throws NTStatusException {
        synchronized (state) {
            if (state.isDirectory() || state.isDeleted() || !state.isDirty()) {
                return;
            }
            try {
                if (!state.isLocalContentLoaded()) {
                    throw new IOException("No complete local copy");
                }
                RandomAccessFile file = temporaryFileRegistry.openTemporaryFile(state);
                long size = file.length();
                if (size != state.getFileInfo().getFileSize()) {
                    throw new IOException("Local size does not match metadata");
                }
                if (size > Integer.MAX_VALUE - 8L) {
                    throw new NTStatusException(0xC00000BB); // Full byte[] API cannot handle this size.
                }
                byte[] bytes = new byte[(int) size];
                file.seek(0);
                file.readFully(bytes);
                uploadFileUseCase.uploadFile(
                        state.getPath(), bytes, "application/octet-stream", state.getVolumeLabel());
                state.setDirty(false);
            } catch (IOException | RuntimeException e) {
                log.error("Upload failed; retaining staging file {} for {}",
                        state.getTemporaryPath(), state.getPath(), e);
                throw new NTStatusException(0xC0000185);
            }
        }
    }
}
