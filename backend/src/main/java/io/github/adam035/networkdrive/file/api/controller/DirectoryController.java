package io.github.adam035.networkdrive.file.api.controller;

import io.github.adam035.networkdrive.file.api.dto.DirectoryCreationRequest;
import io.github.adam035.networkdrive.file.application.service.DirectoryService;
import io.github.adam035.networkdrive.file.domain.model.Directory;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/directories")
public class DirectoryController {

    private DirectoryService directoryService;

    @PostMapping
    public void createDirectory(@RequestBody DirectoryCreationRequest directoryCreationRequest) {
        directoryService.createDirectory(directoryCreationRequest);
    }

    @GetMapping("/{directoryId}")
    public Directory getDirectory(@PathVariable String directoryId) {
        return directoryService.getDirectory(directoryId);
    }

    @DeleteMapping("/{directoryId}")
    public void deleteDirectory(@PathVariable String directoryId) {
        directoryService.deleteDirectory(directoryId);
    }

}
