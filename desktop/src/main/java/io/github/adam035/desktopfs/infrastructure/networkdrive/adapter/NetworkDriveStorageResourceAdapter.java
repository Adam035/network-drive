package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.dto.EditStorageResourceCommand;
import io.github.adam035.desktopfs.application.port.StorageResourcePort;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NetworkDriveStorageResourceAdapter implements StorageResourcePort {

    private final RestClient networkDriveClient;

    @Override
    @Cacheable(cacheNames = "storageResources", key = "#path")
    public StorageResource getStorageResource(String path) {
        return networkDriveClient.get()
                .uri("/storage-resources".concat(path))
                .retrieve()
                .body(StorageResource.class);
    }

    @Override
    @CachePut(cacheNames = "storageResources", key = "#path")
    public StorageResource editStorageResource(String path, EditStorageResourceCommand editStorageResourceCommand) {
        return networkDriveClient.put()
                .uri("/storage-resources".concat(path))
                .body(editStorageResourceCommand)
                .retrieve()
                .body(StorageResource.class);
    }

    @Override
    @CacheEvict(cacheNames = "storageResources", key = "#path")
    public void deleteStorageResource(String path) {
        networkDriveClient.delete()
                .uri("/storage-resources".concat(path))
                .retrieve()
                .toBodilessEntity();
    }

}
