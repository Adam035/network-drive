package io.github.adam035.networkdrive.user.api.dto;

public record UserCreationRequest(
        String username,
        String email
) {
}
