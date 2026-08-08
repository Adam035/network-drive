package io.github.adam035.networkdrive.application.dto;

public record FinishLoginResult(
        AuthTokensResult authTokensResult,
        String message
) {
}
