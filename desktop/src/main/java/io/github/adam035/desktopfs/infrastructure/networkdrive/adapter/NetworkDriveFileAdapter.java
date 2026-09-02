package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.port.FilePort;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.FileUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NetworkDriveFileAdapter implements FilePort {

    private final RestClient networkDriveClient;

    @Override
    public byte[] downloadFile(String path, long offset, int length) {
        return networkDriveClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/files".concat(path))
                        .queryParam("offset", offset)
                        .queryParam("length", length)
                        .build()
                )
                .retrieve()
                .body(byte[].class);
    }

    @Override
    @CacheEvict(cacheNames = "storageResources", allEntries = true)
    public void uploadFile(String path, byte[] bytes, String mimeType) {
        networkDriveClient.post()
                .uri("/files")
                .body(new FileUploadRequest(path, bytes, mimeType))
                .retrieve()
                .toBodilessEntity();
    }

}
