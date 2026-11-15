package io.github.adam035.networkdrive.application.dto;

public record ExchangeCodeCommand(
        String code,
        String codeVerifier
) {
}
