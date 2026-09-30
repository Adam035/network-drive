package io.github.adam035.desktopfs.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.io.RandomAccessFile;

@Getter
@Setter
public class OpenFileState {

    private String path;

    private StorageResource storageResource;

    private long allocationSize;

    private RandomAccessFile temporaryFile;

    private boolean isSynchronized = true;

    public OpenFileState(String path, StorageResource storageResource) {
        this.path = path;
        this.storageResource = storageResource;
        this.allocationSize = storageResource.getSize();
    }

}
