package io.github.adam035.networkdrive.application.dto;

public record UpdateUserCommand(
        String id,
        String username,
        String email,
        String accountTierName
) {
}
