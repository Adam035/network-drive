package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.dto.FileDownloadResult;
import io.github.adam035.desktopfs.application.port.FilePort;
import io.github.adam035.desktopfs.domain.model.File;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.FileUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

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
    public void uploadFile(String path, byte[] bytes, String mimeType) {
        networkDriveClient.post()
                .uri("/files".concat(path))
                .body(new FileUploadRequest(path, bytes, mimeType))
                .retrieve();
    }

}
