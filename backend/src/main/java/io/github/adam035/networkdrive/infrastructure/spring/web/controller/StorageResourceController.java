package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.usecase.DeleteStorageResourceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage-resources")
public class StorageResourceController {

    private final DeleteStorageResourceUseCase deleteStorageResourceUseCase;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStorageResource(@RequestParam String path) {
        deleteStorageResourceUseCase.deleteStorageResource(path);
    }

}
