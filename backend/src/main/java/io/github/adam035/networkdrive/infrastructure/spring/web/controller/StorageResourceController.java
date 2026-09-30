package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.dto.MoveStorageResourceCommand;
import io.github.adam035.networkdrive.application.usecase.DeleteStorageResourceUseCase;
import io.github.adam035.networkdrive.application.usecase.GetStorageResourceUseCase;
import io.github.adam035.networkdrive.application.usecase.MoveStorageResourceUseCase;
import io.github.adam035.networkdrive.domain.model.StorageResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage-resources")
public class StorageResourceController {

    private final GetStorageResourceUseCase getStorageResourceUseCase;

    private final MoveStorageResourceUseCase moveStorageResourceUseCase;

    private final DeleteStorageResourceUseCase deleteStorageResourceUseCase;

    @GetMapping("/**")
    public StorageResource getStorageResource(HttpServletRequest request) {
        String path = request.getRequestURI().replace("/storage-resources", "");
        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
        return getStorageResourceUseCase.getStorageResource(decodedPath);
    }

    @PatchMapping("/move")
    public void moveStorageResource(@RequestBody MoveStorageResourceCommand moveStorageResourceCommand) {
        moveStorageResourceUseCase.moveStorageResource(moveStorageResourceCommand);
    }

    @DeleteMapping("/**")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStorageResource(HttpServletRequest request) {
        String path = request.getRequestURI().replace("/storage-resources", "");
        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
        deleteStorageResourceUseCase.deleteStorageResource(decodedPath);
    }

}
