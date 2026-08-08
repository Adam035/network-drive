package io.github.adam035.networkdrive.application.dto;

public record EditStorageResourceCommand(
        String path,
        Long size,
        String owner,
        String type
) {
}
