package io.github.adam035.networkdrive.auth.api.dto;

public record UserCreationRequest(
        String username,
        String email
) {
}
