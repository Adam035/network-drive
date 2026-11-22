package io.github.adam035.networkdrive.application.dto;

public record RegisterDeviceCommand(
        byte[] publicKey
) {
}
