package io.github.adam035.networkdrive.application.dto;

import io.github.adam035.networkdrive.domain.model.File;

import java.io.InputStream;

public record FileDownloadResult(
        File file,
        InputStream fileContent
) {
}

