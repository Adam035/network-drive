package io.github.adam035.networkdrive.auth.api.dto;

public record LoginRequest(
        String id,
        String authenticatorAttachment,
        LoginResponse response,
        String type
) {
}
