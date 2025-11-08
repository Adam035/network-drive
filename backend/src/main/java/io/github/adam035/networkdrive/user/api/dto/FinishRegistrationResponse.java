package io.github.adam035.networkdrive.user.api.dto;

public record FinishRegistrationResponse(
        boolean isRegistrationSuccessful,
        String message
) {
}
