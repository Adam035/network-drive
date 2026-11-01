package io.github.adam035.desktopfs.infrastructure.networkdrive.dto;

public record MoveStorageResourceRequest(
        String oldPath,
        String newPath,
        boolean replaceIfExists
) {
}
