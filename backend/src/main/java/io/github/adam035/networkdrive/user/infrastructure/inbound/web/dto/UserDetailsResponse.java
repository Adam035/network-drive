package io.github.adam035.networkdrive.user.infrastructure.inbound.web.dto;

public record UserDetailsResponse(
    String id,
    String username,
    String email
) {
}
