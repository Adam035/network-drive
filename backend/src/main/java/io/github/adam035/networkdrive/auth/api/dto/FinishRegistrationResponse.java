package io.github.adam035.networkdrive.auth.api.dto;

import io.github.adam035.networkdrive.auth.domain.model.User;

public record FinishRegistrationResponse(
        User user,
        String message
) {
}
