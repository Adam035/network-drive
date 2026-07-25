package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.usecase.DownloadFileUseCase;
import io.github.adam035.networkdrive.application.usecase.UploadFileUseCase;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FileUploadRequest;
import io.github.adam035.networkdrive.domain.model.File;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final UploadFileUseCase uploadFileUseCase;

    private final DownloadFileUseCase downloadFileUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public File uploadFile(@ModelAttribute FileUploadRequest fileUploadRequest) {
        return uploadFileUseCase.uploadFile(fileUploadRequest.multipartFile(), fileUploadRequest.path());
    }

    @GetMapping("/**")
    public byte[] downloadFile(
            @RequestParam(required = false) Long offset,
            @RequestParam(required = false) Long length,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI().replace("/files", "");
        return downloadFileUseCase.downloadFile(path, offset, length);
    }

}
