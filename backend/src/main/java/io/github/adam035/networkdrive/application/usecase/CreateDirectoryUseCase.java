package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.StorageResourceNotFoundException;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.domain.service.DirectoryService;
import io.github.adam035.networkdrive.domain.service.StorageResourceAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateDirectoryUseCase {

    private final AuthUserExtractorPort authUserExtractorPort;

    private final StorageResourceAccessService storageResourceAccessService;

    private final DirectoryRepository directoryRepository;

    private final DirectoryService directoryService;

    public Directory createDirectory(String path) {
        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        Directory parentDirectory = directoryService.findParentDirectoryByPath(path)
                .orElseThrow(StorageResourceNotFoundException::new);

        if (!storageResourceAccessService.canAccess(parentDirectory, user)) {
            throw new UnauthorizedException();
        }

        return directoryRepository.save(directoryService.createDirectory(path, parentDirectory, user));
    }

}
