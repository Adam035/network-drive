package io.github.adam035.networkdrive.user.api.dto;

public record LoginRequest(
        String id,
        String authenticatorAttachment,
        LoginResponse response,
        String type
) {
}
