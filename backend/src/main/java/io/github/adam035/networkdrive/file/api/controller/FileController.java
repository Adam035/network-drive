package io.github.adam035.networkdrive.file.api.controller;

import io.github.adam035.networkdrive.file.application.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final TransferService transferService;

    @PostMapping("/upload")
    public void upload(
            @RequestParam String parentId,
            @RequestParam MultipartFile file
    ) {

    }

    @GetMapping("/{id}/download")
    public void download(@PathVariable String id) {

    }

}
