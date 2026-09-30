package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.MoveStorageResourceCommand;
import io.github.adam035.networkdrive.application.exception.StorageResourceAlreadyExistsException;
import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.StorageResourceNotFoundException;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.StorageResource;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.domain.repository.StorageResourceRepository;
import io.github.adam035.networkdrive.domain.service.DirectoryService;
import io.github.adam035.networkdrive.domain.service.StorageResourceAccessService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.github.adam035.networkdrive.domain.model.StorageResource.Type.DIRECTORY;

@Slf4j
@Service
@AllArgsConstructor
public class MoveStorageResourceUseCase {

    private final AuthUserExtractorPort authUserExtractorPort;

    private final StorageResourceAccessService storageResourceAccessService;

    private final DirectoryService directoryService;

    private final StorageResourceRepository storageResourceRepository;

    private final DirectoryRepository directoryRepository;

    private final DeleteStorageResourceUseCase deleteStorageResourceUseCase;

    @Transactional
    public void moveStorageResource(MoveStorageResourceCommand moveStorageResourceCommand) {
        String oldPath = moveStorageResourceCommand.oldPath();
        String newPath = moveStorageResourceCommand.newPath();

        StorageResource storageResource = storageResourceRepository.findByPath(oldPath)
                .orElseThrow(() -> new StorageResourceNotFoundException(oldPath));

        if (oldPath.equals(newPath)) {
            return;
        }

        String oldParentPath = oldPath.substring(0, oldPath.lastIndexOf('/'));
        String newParentPath = newPath.substring(0, newPath.lastIndexOf('/'));

        if (oldParentPath.equals(newParentPath)) {
            renameStorageResource(storageResource, newPath);
            return;
        }

        if (DIRECTORY.equals(storageResource.getType())) {
            throw new UnsupportedOperationException("Moving directories requires updating all descendant paths"); // TODO
        }

        Directory oldParent = directoryRepository.findByPath(oldParentPath)
                .orElseThrow(() -> new StorageResourceNotFoundException(oldParentPath));

        Directory newParent = directoryRepository.findByPath(newParentPath)
                .orElseThrow(() -> new StorageResourceNotFoundException(newParentPath));

        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        boolean canMove = storageResourceAccessService.canAccess(storageResource, user)
                && storageResourceAccessService.canAccess(oldParent, user)
                && storageResourceAccessService.canAccess(newParent, user);

        if (!canMove) {
            throw new UnauthorizedException();
        }

        storageResourceRepository.findByPath(newPath)
                .ifPresent(existingStorageResource -> {
                    if (!moveStorageResourceCommand.replaceIfExists()) {
                        throw new StorageResourceAlreadyExistsException(newPath);
                    }

                    deleteStorageResourceUseCase.deleteStorageResource(newPath);
                });

        directoryService.addStorageResource(newParent, storageResource);

        storageResourceRepository.save(storageResource);
    }

    private void renameStorageResource(StorageResource storageResource, String newPath) {
        String name = newPath.substring(newPath.lastIndexOf('/') + 1);
        storageResource.setName(name);
        storageResource.setPath(newPath);
        storageResourceRepository.save(storageResource);
    }

}
