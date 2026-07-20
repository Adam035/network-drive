package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.usecase.DeleteStorageResourceUseCase;
import io.github.adam035.networkdrive.application.usecase.GetStorageResourceUseCase;
import io.github.adam035.networkdrive.domain.model.StorageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage-resources")
public class StorageResourceController {

    private final GetStorageResourceUseCase getStorageResourceUseCase;

    private final DeleteStorageResourceUseCase deleteStorageResourceUseCase;

    @GetMapping
    public StorageResource getStorageResource(@RequestParam String path) {
        return getStorageResourceUseCase.getStorageResource(path);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStorageResource(@RequestParam String path) {
        deleteStorageResourceUseCase.deleteStorageResource(path);
    }

}
