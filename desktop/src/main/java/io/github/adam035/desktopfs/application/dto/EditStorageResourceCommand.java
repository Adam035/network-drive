package io.github.adam035.desktopfs.application.dto;

import lombok.Builder;

@Builder
public record EditStorageResourceCommand(
        String path,
        Long size,
        String owner,
        String type
) {
}
