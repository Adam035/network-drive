package io.github.adam035.desktopfs.domain.service;

import io.github.adam035.desktopfs.domain.model.OpenFileState;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.IdentityHashMap;
import java.util.Map;

@Component
public class TemporaryFileService {

    private final static String PREFIX = "network-drive-";

    private final static String SUFIX = ".tmp";

    private final static Path STAGING_DIRECTORY = Path.of(
            System.getProperty("user.home"),
            "AppData",
            "Local",
            "NetworkDrive",
            "staging"
    );

    private final Map<OpenFileState, Path> temporaryPaths = new IdentityHashMap<>();

    public synchronized void truncate(OpenFileState openFileState) throws IOException {
        RandomAccessFile randomAccessFile = openTemporaryFile(openFileState);
        randomAccessFile.setLength(0);
        randomAccessFile.seek(0);
    }

    public synchronized long length(OpenFileState openFileState) throws IOException {
        return openTemporaryFile(openFileState).length();
    }

    public synchronized void setLength(OpenFileState openFileState, long length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length cannot be negative");
        }

        openTemporaryFile(openFileState).setLength(length);
    }

    public synchronized void write(OpenFileState openFileState, long offset, byte[] bytes) throws IOException {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative");
        }

        RandomAccessFile randomAccessFile = openTemporaryFile(openFileState);
        randomAccessFile.seek(offset);
        randomAccessFile.write(bytes);
    }

    public synchronized byte[] read(OpenFileState openFileState, long offset, int length) throws IOException {
        if (offset < 0 || length < 0) {
            throw new IllegalArgumentException("Offset and length cannot be negative");
        }

        RandomAccessFile randomAccessFile = openTemporaryFile(openFileState);

        int bytesToRead = Math.clamp(randomAccessFile.length() - offset, 0, length);
        byte[] bytes = new byte[bytesToRead];

        randomAccessFile.seek(offset);
        randomAccessFile.readFully(bytes);

        return bytes;
    }

    public synchronized void close(OpenFileState openFileState) throws IOException {
        RandomAccessFile file = openFileState.getTemporaryFile();

        if (file != null) {
            file.close();
            openFileState.setTemporaryFile(null);
        }

        Path temporaryPath = temporaryPaths.get(openFileState);

        if (temporaryPath != null) {
            Files.deleteIfExists(temporaryPath);
            temporaryPaths.remove(openFileState);
        }
    }

    private RandomAccessFile openTemporaryFile(OpenFileState openFileState) throws IOException {
        RandomAccessFile randomAccessFile = openFileState.getTemporaryFile();

        if (randomAccessFile != null) {
            return randomAccessFile;
        }

        Path temporaryPath = temporaryPaths.get(openFileState);

        if (temporaryPath == null) {
            Files.createDirectories(STAGING_DIRECTORY);
            temporaryPath = Files.createTempFile(STAGING_DIRECTORY, PREFIX, SUFIX);
            temporaryPaths.put(openFileState, temporaryPath);
        }

        RandomAccessFile newRandomAccessFile = new RandomAccessFile(temporaryPath.toFile(), "rw");
        openFileState.setTemporaryFile(newRandomAccessFile);

        return newRandomAccessFile;
    }

}
