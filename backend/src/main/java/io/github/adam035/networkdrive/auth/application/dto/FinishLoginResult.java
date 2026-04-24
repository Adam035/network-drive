package io.github.adam035.networkdrive.auth.application.dto;

public record FinishLoginResult(
        AuthTokensResult authTokensResult,
        String message
) {
}
