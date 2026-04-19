package io.github.adam035.networkdrive.auth.api.dto;

public record RotateTokensRequest(
        String userId,
        String refreshToken
) {
}
