package io.github.adam035.networkdrive.auth.application.dto;

import io.github.adam035.networkdrive.common.domain.model.User;

public record StartRegistrationCommand(
        User user
) {
}
