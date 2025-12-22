package io.github.adam035.networkdrive.file.api.dto;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        MultipartFile file
) {
}
