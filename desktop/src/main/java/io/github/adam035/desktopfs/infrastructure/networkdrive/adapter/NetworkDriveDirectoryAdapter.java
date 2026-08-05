package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.dto.ReadDirectoryResult;
import io.github.adam035.desktopfs.application.port.DirectoryPort;
import io.github.adam035.desktopfs.domain.model.Directory;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.CreateDirectoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NetworkDriveDirectoryAdapter implements DirectoryPort {

    private final RestClient networkDriveClient;

    @Override
    public Directory createDirectory(String path) {
        return networkDriveClient.post()
                .uri("/directories")
                .body(new CreateDirectoryRequest(path))
                .retrieve()
                .body(Directory.class);
    }

    @Override
    public ReadDirectoryResult readDirectory(String path) {
        System.out.println("readdirecotry");
        System.out.println(path);
        return networkDriveClient.get()
                .uri("/directories".concat(path))
                .retrieve()
                .body(ReadDirectoryResult.class);
    }

}
