package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.usecase.DownloadFileUseCase;
import io.github.adam035.networkdrive.application.usecase.UploadFileUseCase;
import io.github.adam035.networkdrive.application.dto.FileDownloadResult;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FileUploadRequest;
import io.github.adam035.networkdrive.domain.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String path) {
        FileDownloadResult fileDownloadResult = downloadFileUseCase.downloadFile(path);

        return ResponseEntity.ok()
                .contentLength(fileDownloadResult.file().getSize())
                .contentType(MediaType.parseMediaType(fileDownloadResult.file().getMimeType()))
                .header("X-File-Created-At", fileDownloadResult.file().getCreatedAt().toString())
                .header("X-File-Updated-At", fileDownloadResult.file().getUpdatedAt().toString())
                .body(new InputStreamResource(fileDownloadResult.fileContent()));
    }

}
