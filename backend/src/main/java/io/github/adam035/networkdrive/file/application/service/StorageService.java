package io.github.adam035.networkdrive.file.application.service;

import java.io.InputStream;

interface StorageService {

    void uploadFile(String storageKey, InputStream inputStream, long size, String contentType);

    InputStream downloadFile(String storageKey);

    void deleteFile(String storageKey);

}
