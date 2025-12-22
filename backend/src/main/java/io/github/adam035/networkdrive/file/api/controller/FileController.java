package io.github.adam035.networkdrive.file.api.controller;

import io.github.adam035.networkdrive.file.api.dto.FileDownloadResponse;
import io.github.adam035.networkdrive.file.api.dto.FileUploadRequest;
import io.github.adam035.networkdrive.file.application.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public void upload(@ModelAttribute FileUploadRequest fileUploadRequest) {
        fileService.uploadFile(fileUploadRequest);
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable String fileId) {
        FileDownloadResponse fileDownloadResponse = fileService.downloadFile(fileId);

        return ResponseEntity.ok()
                .contentLength(fileDownloadResponse.file().getSize())
                .contentType(MediaType.parseMediaType(fileDownloadResponse.file().getMimeType()))
                .header("X-File-Created-At", fileDownloadResponse.file().getCreatedAt().toString())
                .header("X-File-Updated-At", fileDownloadResponse.file().getUpdatedAt().toString())
                .body(new InputStreamResource(fileDownloadResponse.fileContent()));
    }

}
