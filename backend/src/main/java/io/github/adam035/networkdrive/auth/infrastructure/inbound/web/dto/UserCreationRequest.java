package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto;

public record UserCreationRequest(
        String username,
        String email
) {
}
