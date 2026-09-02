package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.port.StorageResourcePort;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.MoveStorageResourceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NetworkDriveStorageResourceAdapter implements StorageResourcePort {

    private final RestClient networkDriveClient;

    @Override
    @Cacheable(cacheNames = "storageResources", key = "#path", unless = "#result == null")
    public Optional<StorageResource> getStorageResource(String path) {
        try {
            StorageResource storageResource = networkDriveClient.get()
                    .uri("/storage-resources".concat(path))
                    .retrieve()
                    .body(StorageResource.class);

            return Optional.ofNullable(storageResource);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    @CacheEvict(cacheNames = "storageResources", allEntries = true)
    public void moveStorageResource(String oldPath, String newPath, boolean replaceIfExists) {
        networkDriveClient.patch()
                .uri("/storage-resources/move")
                .body(new MoveStorageResourceRequest(oldPath, newPath, replaceIfExists))
                .retrieve()
                .body(StorageResource.class);
    }

    @Override
    @CacheEvict(cacheNames = "storageResources", allEntries = true)
    public void deleteStorageResource(String path) {
        networkDriveClient.delete()
                .uri("/storage-resources".concat(path))
                .retrieve()
                .toBodilessEntity();
    }

}
