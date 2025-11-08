package io.github.adam035.networkdrive.user.api.dto;

public record LoginResponse(
        String authenticatorData,
        String clientDataJSON,
        String signature,
        String userHandle
) {
}
