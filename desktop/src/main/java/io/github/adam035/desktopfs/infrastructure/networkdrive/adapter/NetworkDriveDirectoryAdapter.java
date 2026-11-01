package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.dto.ReadDirectoryResult;
import io.github.adam035.desktopfs.application.port.DirectoryPort;
import io.github.adam035.desktopfs.domain.model.Directory;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.CreateDirectoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NetworkDriveDirectoryAdapter implements DirectoryPort {

    private final RestClient networkDriveClient;

    @Override
    @CacheEvict(cacheNames = "directoryListings", key = "#path.substring(0, #path.lastIndexOf('/'))")
    public Directory createDirectory(String path) {
        return networkDriveClient.post()
                .uri("/directories")
                .body(new CreateDirectoryRequest(path))
                .retrieve()
                .body(Directory.class);
    }

    @Override
    @Cacheable(cacheNames = "directoryListings", key = "#path")
    public ReadDirectoryResult readDirectory(String path) {
        return networkDriveClient.get()
                .uri("/directories".concat(path))
                .retrieve()
                .body(ReadDirectoryResult.class);
    }

}
