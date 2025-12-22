package io.github.adam035.networkdrive.file.application.service;

import io.github.adam035.networkdrive.file.domain.model.File;

import java.io.InputStream;

interface StorageService {

    void uploadFile(File file, InputStream inputStream);

    InputStream downloadFile(String storageKey);

    void deleteFile(String storageKey);

}
