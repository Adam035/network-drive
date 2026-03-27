package io.github.adam035.networkdrive.file.api.dto;

import io.github.adam035.networkdrive.file.domain.model.File;

import java.io.InputStream;

public record FileDownloadResponse(
        File file,
        InputStream fileContent
) {
}

