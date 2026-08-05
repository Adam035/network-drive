package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.port.StorageResourcePort;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NetworkDriveStorageResourceAdapter implements StorageResourcePort {

    private final RestClient networkDriveClient;

    @Override
    public StorageResource getStorageResource(String path) {
        return networkDriveClient.get()
                .uri("/storage-resources".concat(path))
                .retrieve()
                .body(StorageResource.class);
    }

    @Override
    public void deleteStorageResource(String path) {
        networkDriveClient.delete()
                .uri("/storage-resources".concat(path))
                .retrieve()
                .body(Void.class);
    }

}
