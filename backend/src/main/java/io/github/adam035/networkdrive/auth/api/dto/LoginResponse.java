package io.github.adam035.networkdrive.auth.api.dto;

public record LoginResponse(
        String authenticatorData,
        String clientDataJSON,
        String signature,
        String userHandle
) {
}
