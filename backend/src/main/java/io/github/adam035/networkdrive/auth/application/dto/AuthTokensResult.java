package io.github.adam035.networkdrive.auth.application.dto;

public record AuthTokensResult(
        String accessToken,
        String refreshToken
) {
}
