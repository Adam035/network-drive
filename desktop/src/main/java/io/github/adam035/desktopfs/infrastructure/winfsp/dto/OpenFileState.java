package io.github.adam035.desktopfs.infrastructure.winfsp.dto;

import com.github.jnrwinfspteam.jnrwinfsp.api.FileInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.RandomAccessFile;

@Getter
@Setter
public class OpenFileState {

    private String path;

    private FileInfo fileInfo;

    private RandomAccessFile temporaryFile;

    private boolean isSynchronized = true;

    public OpenFileState(String path, FileInfo fileInfo) {
        this.path = path;
        this.fileInfo = fileInfo;
    }

}
