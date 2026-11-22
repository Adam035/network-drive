package io.github.adam035.desktopfs.application.dto;

public record KeyPair(
        byte[] privateKey,
        byte[] publicKey
) {
}
