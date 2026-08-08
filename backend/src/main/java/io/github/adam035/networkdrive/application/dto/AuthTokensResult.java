package io.github.adam035.networkdrive.application.dto;

public record AuthTokensResult(
        String accessToken,
        String refreshToken
) {
}
