package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

public record UserDetailsResponse(
    String id,
    String username,
    String email
) {
}
