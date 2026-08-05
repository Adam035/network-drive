package io.github.adam035.desktopfs.application.port;

import io.github.adam035.desktopfs.domain.model.StorageResource;

public interface StorageResourcePort {

    StorageResource getStorageResource(String path);

    void deleteStorageResource(String path);

}
