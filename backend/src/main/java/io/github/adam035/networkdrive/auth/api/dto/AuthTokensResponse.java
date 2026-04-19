package io.github.adam035.networkdrive.auth.api.dto;

public record AuthTokensResponse(
        String accessToken,
        String refreshToken
) {
}
