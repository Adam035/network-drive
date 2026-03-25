package io.github.adam035.networkdrive.file.api.dto;

public record DirectoryCreationRequest(
        String parentId,
        String name
) {
}
