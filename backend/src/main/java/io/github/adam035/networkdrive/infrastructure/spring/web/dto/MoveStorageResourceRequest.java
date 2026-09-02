package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

public record MoveStorageResourceRequest(
        String oldPath,
        String newPath,
        boolean replaceIfExists
) {
}
