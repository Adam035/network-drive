package io.github.adam035.networkdrive.application.port;

import io.github.adam035.networkdrive.domain.model.File;

import java.io.InputStream;
import java.util.List;

public interface StoragePort {

    void uploadFile(File file, InputStream inputStream);

    InputStream downloadFile(String storageKey);

    void deleteFile(String storageKey);

    void deleteFiles(List<String> storageKeys);

}
