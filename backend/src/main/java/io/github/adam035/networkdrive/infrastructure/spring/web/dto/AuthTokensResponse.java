package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

public record AuthTokensResponse(
        String accessToken,
        String refreshToken
) {
}
