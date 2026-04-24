package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto;

public record FinishLoginResponse(
        AuthTokensResponse authTokens,
        String message
) {
}
