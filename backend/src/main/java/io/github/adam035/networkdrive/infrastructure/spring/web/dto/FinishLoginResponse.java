package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

public record FinishLoginResponse(
        AuthTokensResponse authTokens,
        String message
) {
}
