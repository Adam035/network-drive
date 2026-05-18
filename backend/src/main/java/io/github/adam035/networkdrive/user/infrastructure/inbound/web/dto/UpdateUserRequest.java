package io.github.adam035.networkdrive.user.infrastructure.inbound.web.dto;

public record UpdateUserRequest(
        String username,
        String email
) {
}
