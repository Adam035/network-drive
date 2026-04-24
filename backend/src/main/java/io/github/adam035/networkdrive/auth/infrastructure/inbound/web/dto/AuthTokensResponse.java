package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto;

public record AuthTokensResponse(
        String accessToken,
        String refreshToken
) {
}
