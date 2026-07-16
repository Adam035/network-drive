package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.ReadDirectoryResult;
import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.StorageResourceNotFoundException;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.StorageResource;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.domain.repository.StorageResourceRepository;
import io.github.adam035.networkdrive.domain.service.StorageResourceAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ReadDirectoryUseCase {

    private final DirectoryRepository directoryRepository;

    private final AuthUserExtractorPort authUserExtractorPort;

    private final StorageResourceAccessService storageResourceAccessService;

    private final StorageResourceRepository storageResourceRepository;

    public ReadDirectoryResult readDirectory(String path) {
        Directory directory = directoryRepository.findByPath(String.format("/%s", path))
                .orElseThrow(StorageResourceNotFoundException::new);

        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        if (!storageResourceAccessService.canAccess(directory, user)) {
            throw new UnauthorizedException();
        }

        List<StorageResource> children = storageResourceRepository.findAllByParent(directory);

        return new ReadDirectoryResult(directory, children);
    }

}
