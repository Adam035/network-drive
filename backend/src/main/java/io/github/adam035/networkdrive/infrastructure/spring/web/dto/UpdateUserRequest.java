package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

public record UpdateUserRequest(
        String username,
        String email
) {
}
