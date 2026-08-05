package io.github.adam035.desktopfs.application.dto;

import io.github.adam035.desktopfs.domain.model.Directory;
import io.github.adam035.desktopfs.domain.model.StorageResource;

import java.util.List;

public record ReadDirectoryResult(
        Directory directory,
        List<StorageResource> children
) {
}
