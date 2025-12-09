package io.github.adam035.networkdrive.file.application.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@AllArgsConstructor
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;

    private final String bucket;

    @Override
    public void uploadFile(String storageKey, InputStream inputStream, long size, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(storageKey)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            // TODO
        }
    }

    @Override
    public InputStream downloadFile(String storageKey) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(storageKey)
                            .build()
            );
        } catch (Exception e) {
            // TODO
            return null;
        }
    }

    @Override
    public void deleteFile(String storageKey) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(storageKey)
                            .build()
            );
        } catch (Exception e) {
            // TODO
        }
    }

}
