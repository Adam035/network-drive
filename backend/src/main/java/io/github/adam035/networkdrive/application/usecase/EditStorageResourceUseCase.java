package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.EditStorageResourceCommand;
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

@Component
@RequiredArgsConstructor
public class EditStorageResourceUseCase {

    private final DirectoryRepository directoryRepository;

    private final AuthUserExtractorPort authUserExtractorPort;

    private final StorageResourceAccessService storageResourceAccessService;

    private final StorageResourceRepository storageResourceRepository;

    public StorageResource editStorageResource(String path, EditStorageResourceCommand editStorageResourceCommand) {
        String targetDirectoryPath = editStorageResourceCommand.path()
                .substring(0, editStorageResourceCommand.path().lastIndexOf('/'));

        StorageResource storageResource = storageResourceRepository.findByPath(path)
                .orElseThrow(() -> new StorageResourceNotFoundException(path));

        Directory directory = directoryRepository.findByPath(targetDirectoryPath)
                .orElseThrow(() -> new StorageResourceNotFoundException(targetDirectoryPath));

        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        boolean canAccess = storageResourceAccessService.canAccess(storageResource, user)
                && storageResourceAccessService.canAccess(directory, user);

        if (!canAccess) {
            throw new UnauthorizedException();
        }

        editMetadata(editStorageResourceCommand, storageResource);

        return storageResourceRepository.save(storageResource);
    }

    private void editMetadata(
            EditStorageResourceCommand editStorageResourceCommand,
            StorageResource storageResource
    ) {
        String path = editStorageResourceCommand.path();

        if (path != null) {
            String name = path.substring(path.lastIndexOf('/') + 1);
            storageResource.setName(name);
            storageResource.setPath(path);
        }

        if (editStorageResourceCommand.size() != null) {
            storageResource.setSize(editStorageResourceCommand.size());
        }

        if (editStorageResourceCommand.owner() != null) {
            authUserExtractorPort.extractUser()
                    .ifPresentOrElse(storageResource::setOwner, UserDoesNotExist::new);
        }

        if (editStorageResourceCommand.type() != null) {
            storageResource.setType(StorageResource.Type.valueOf(editStorageResourceCommand.type()));
        }
    }

}
