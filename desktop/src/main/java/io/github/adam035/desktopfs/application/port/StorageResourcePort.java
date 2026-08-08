package io.github.adam035.desktopfs.application.port;

import io.github.adam035.desktopfs.application.dto.EditStorageResourceCommand;
import io.github.adam035.desktopfs.domain.model.StorageResource;

public interface StorageResourcePort {

    StorageResource getStorageResource(String path);

    StorageResource editStorageResource(String path, EditStorageResourceCommand editStorageResourceCommand);

    void deleteStorageResource(String path);

}
