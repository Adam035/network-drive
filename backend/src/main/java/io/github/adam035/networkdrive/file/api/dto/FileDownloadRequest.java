package io.github.adam035.networkdrive.file.api.dto;

import java.io.InputStream;

public record FileDownloadRequest(
        InputStream stream,
        String filename,
        String mimeType,
        long size
) {
}

