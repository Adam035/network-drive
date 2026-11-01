package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        MultipartFile multipartFile,
        String path
) {
}
