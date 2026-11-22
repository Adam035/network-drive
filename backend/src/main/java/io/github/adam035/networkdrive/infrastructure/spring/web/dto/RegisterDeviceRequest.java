package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

public record RegisterDeviceRequest(
        String deviceId,
        String publicKeyBase64Url
) {
}
