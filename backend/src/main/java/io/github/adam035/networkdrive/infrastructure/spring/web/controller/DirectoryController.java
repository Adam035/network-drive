package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.dto.ReadDirectoryResult;
import io.github.adam035.networkdrive.application.usecase.CreateDirectoryUseCase;
import io.github.adam035.networkdrive.application.usecase.ReadDirectoryUseCase;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.DirectoryCreationRequest;
import io.github.adam035.networkdrive.domain.model.Directory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directories")
public class DirectoryController {

    private final CreateDirectoryUseCase createDirectoryUseCase;

    private final ReadDirectoryUseCase readDirectoryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Directory createDirectory(@RequestBody DirectoryCreationRequest directoryCreationRequest) {
        return createDirectoryUseCase.createDirectory(directoryCreationRequest.path());
    }

    @GetMapping("/**")
    public ReadDirectoryResult readDirectory(HttpServletRequest request) {
        String path = request.getRequestURI().replace("/directories", "");
        return readDirectoryUseCase.readDirectory(path);
    }

}
