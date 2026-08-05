package io.github.adam035.desktopfs.application.dto;

import io.github.adam035.desktopfs.domain.model.File;

import java.io.InputStream;

public record FileDownloadResult(
        File file,
        InputStream fileContent
) {
}
