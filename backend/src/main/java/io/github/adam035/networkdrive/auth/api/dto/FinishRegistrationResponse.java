package io.github.adam035.networkdrive.auth.api.dto;

public record FinishRegistrationResponse(
        boolean isRegistrationSuccessful,
        String message
) {
}
