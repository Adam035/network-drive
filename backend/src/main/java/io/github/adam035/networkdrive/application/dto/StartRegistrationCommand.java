package io.github.adam035.networkdrive.application.dto;

import io.github.adam035.networkdrive.domain.model.User;

public record StartRegistrationCommand(
        User user
) {
}
