package io.github.adam035.desktopfs.infrastructure.winfsp.filesystem;

import com.github.jnrwinfspteam.jnrwinfsp.api.NTStatusException;
import org.springframework.stereotype.Component;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RandomAccessService {

    private final Map<String, RandomAccessFile> randomAccessFiles;

    public RandomAccessService() {
        randomAccessFiles = new ConcurrentHashMap<>();
    }

    private void createTempFile(String path) throws NTStatusException {
        try {
            String prefix = UUID.randomUUID().toString();
            File tempFile = Files.createTempFile(prefix, ".tmp").toFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "rw");
            randomAccessFiles.put(path, randomAccessFile);
        } catch (IOException e) {
            throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
        }
    }

    public void getTempFile(String path) throws NTStatusException {
        if (!randomAccessFiles.containsKey(path)) {
            createTempFile(path);
        }
    }

    public void write(String path, byte[] bytes, long offset) throws NTStatusException {
        if (!randomAccessFiles.containsKey(path)) {
            return;
        }

        try {
            RandomAccessFile randomAccessFile = randomAccessFiles.get(path);
            randomAccessFile.seek(offset);
            randomAccessFile.write(bytes);
        } catch (IOException e) {
            throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
        }
    }

    public byte[] read(String path, long offset, int length) throws NTStatusException {
        byte[] bytes = new byte[length];

        try {
            RandomAccessFile randomAccessFile = randomAccessFiles.get(path);
            randomAccessFile.seek(offset);
            randomAccessFile.readFully(bytes);

            return bytes;
        } catch (EOFException e) {
            throw new NTStatusException(0xC0000011); // STATUS_END_OF_FILE
        } catch (IOException e) {
            throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
        }
    }

    public byte[] readAll(String path) {
        try {
            RandomAccessFile randomAccessFile = randomAccessFiles.get(path);
            byte[] bytes = new byte[(int) randomAccessFile.length()];

            randomAccessFile.seek(0);
            randomAccessFile.readFully(bytes);

            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(String path) throws NTStatusException {
        if (!randomAccessFiles.containsKey(path)) {
            return;
        }

        try {
            if (randomAccessFiles.containsKey(path)) {
                randomAccessFiles.remove(path).close();
            }
        } catch (IOException e) {
            throw new NTStatusException(0xC0000185); // STATUS_IO_DEVICE_ERROR
        }
    }

}
