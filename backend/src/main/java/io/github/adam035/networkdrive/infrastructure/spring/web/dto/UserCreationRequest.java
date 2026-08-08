package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

public record UserCreationRequest(
        String username,
        String email
) {
}
