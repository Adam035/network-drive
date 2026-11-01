package io.github.adam035.desktopfs.infrastructure.networkdrive.dto;

public record RotateAuthTokensResponse(
        String accessToken,
        String refreshToken
) {
}
