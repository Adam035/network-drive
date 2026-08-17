package io.github.adam035.desktopfs.application.port;

import io.github.adam035.desktopfs.domain.model.StorageResource;

import java.util.Optional;

public interface StorageResourcePort {

    Optional<StorageResource> getStorageResource(String path);

    void moveStorageResource(String oldPath, String newPath, boolean replaceIfExists);

    void deleteStorageResource(String path);

}
