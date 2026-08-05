package io.github.adam035.desktopfs.infrastructure.networkdrive.dto;

public record FileUploadRequest(
        String path,
        byte[] bytes,
        String mimeType
) {
}
