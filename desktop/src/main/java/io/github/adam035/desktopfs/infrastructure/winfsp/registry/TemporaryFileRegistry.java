package io.github.adam035.desktopfs.infrastructure.winfsp.registry;

import io.github.adam035.desktopfs.application.usecase.DownloadFileUseCase;
import io.github.adam035.desktopfs.infrastructure.winfsp.dto.OpenFileState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class TemporaryFileRegistry {
    private static final int DOWNLOAD_CHUNK_SIZE = 1024 * 1024;
    private final DownloadFileUseCase downloadFileUseCase;

    public RandomAccessFile openTemporaryFile(OpenFileState state) throws IOException {
        if (state.getTemporaryFile() != null) {
            return state.getTemporaryFile();
        }

        if (state.getTemporaryPath() == null) {
            String localAppData = System.getenv("LOCALAPPDATA");
            Path root = Path.of(localAppData == null || localAppData.isBlank()
                    ? System.getProperty("java.io.tmpdir") : localAppData);
            Path staging = root.resolve("NetworkDrive").resolve("staging");
            Files.createDirectories(staging);
            state.setTemporaryPath(Files.createTempFile(staging, "network-drive-", ".tmp"));
        }

        RandomAccessFile file = new RandomAccessFile(state.getTemporaryPath().toFile(), "rw");
        state.setTemporaryFile(file);
        return file;
    }

    public void ensureLoaded(OpenFileState state) throws IOException {
        if (state.isDirectory() || state.isDeleted()) {
            throw new IOException("Resource is not a writable file: " + state.getPath());
        }

        RandomAccessFile file = openTemporaryFile(state);

        if (state.isLocalContentLoaded()) {
            return;
        }

        file.setLength(0);
        file.seek(0);
        long expectedSize = state.getFileInfo().getFileSize();
        long offset = 0;

        while (offset < expectedSize) {
            int length = (int) Math.min(DOWNLOAD_CHUNK_SIZE, expectedSize - offset);
            byte[] bytes = downloadFileUseCase.downloadFile(state.getPath(), offset, length, state.getVolumeLabel());

            if (bytes == null || bytes.length == 0 || bytes.length > length) {
                throw new IOException("Incomplete download of " + state.getPath());
            }

            file.write(bytes);
            offset += bytes.length;
        }

        state.setLocalContentLoaded(true);
    }

    public void truncate(OpenFileState state) throws IOException {
        RandomAccessFile file = openTemporaryFile(state);
        file.setLength(0);
        file.seek(0);
        state.setLocalContentLoaded(true);
    }

    public byte[] read(OpenFileState state, long offset, int length) throws IOException {
        RandomAccessFile file = openTemporaryFile(state);
        int count = Math.clamp(file.length() - offset, 0, length);
        byte[] bytes = new byte[count];
        file.seek(offset);
        file.readFully(bytes);
        return bytes;
    }

    public void close(OpenFileState state, boolean delete) throws IOException {
        if (state.getTemporaryFile() != null) {
            state.getTemporaryFile().close();
            state.setTemporaryFile(null);
        }

        if (delete && state.getTemporaryPath() != null) {
            Files.deleteIfExists(state.getTemporaryPath());
            state.setTemporaryPath(null);
            state.setLocalContentLoaded(false);
        }
    }

}
