package io.github.adam035.networkdrive.auth.api.dto;

public record FinishLoginResponse(
        AuthTokensResponse authTokens,
        String message
) {
}
