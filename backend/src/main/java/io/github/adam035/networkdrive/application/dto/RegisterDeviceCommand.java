package io.github.adam035.networkdrive.application.dto;

public record RegisterDeviceCommand(
        String deviceId,
        byte[] publicKey
) {
}
