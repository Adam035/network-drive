package io.github.adam035.networkdrive.application.dto;

import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.StorageResource;

import java.util.List;

public record ReadDirectoryResult(
        Directory directory,
        List<StorageResource> children
) {
}
