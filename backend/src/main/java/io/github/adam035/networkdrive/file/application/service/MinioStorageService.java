package io.github.adam035.networkdrive.file.application.service;

import io.github.adam035.networkdrive.file.domain.model.File;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@AllArgsConstructor
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;

    private final String bucket;

    @Override
    public void uploadFile(File file, InputStream inputStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(file.getStorageKey())
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getMimeType())
                            .build()
            );
            log.info(
                    "File {} uploaded successfully to MinIO with storage key {}",
                    file.getName(),
                    file.getStorageKey()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public InputStream downloadFile(String storageKey) {
        try {
             InputStream fileContent = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(storageKey)
                            .build()
            );
            log.info("File with storage key {} downloaded successfully from MinIO", storageKey);
            return fileContent;
        } catch (Exception e) {
            log.error(e.getMessage());
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
            log.info("File with storage key {} deleted successfully from MinIO", storageKey);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
