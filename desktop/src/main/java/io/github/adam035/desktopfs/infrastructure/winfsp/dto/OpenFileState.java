package io.github.adam035.desktopfs.infrastructure.winfsp.dto;

import com.github.jnrwinfspteam.jnrwinfsp.api.FileInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.RandomAccessFile;
import java.nio.file.Path;

@Getter
@Setter
public class OpenFileState {
    private final String volumeLabel;
    private String path;
    private final boolean directory;
    private FileInfo fileInfo;
    private Path temporaryPath;
    private RandomAccessFile temporaryFile;
    private boolean localContentLoaded;
    private boolean dirty;
    private boolean deleted;
    private int openHandleCount;
    private byte[] securityDescriptor;

    public OpenFileState(String volumeLabel, String path, boolean directory, FileInfo fileInfo) {
        this.volumeLabel = volumeLabel;
        this.path = path;
        this.directory = directory;
        this.fileInfo = fileInfo;
    }

    public static long allocationSize(long size) {
        if (size < 0 || size > Long.MAX_VALUE - 511) {
            throw new IllegalArgumentException("Invalid file size: " + size);
        }
        return ((size + 511) / 512) * 512;
    }
}
