package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.application.port.StoragePort;
import io.github.adam035.networkdrive.domain.exception.StorageResourceNotFoundException;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.File;
import io.github.adam035.networkdrive.domain.model.StorageResource;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.domain.repository.FileRepository;
import io.github.adam035.networkdrive.domain.repository.StorageResourceRepository;
import io.github.adam035.networkdrive.domain.service.StorageResourceAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.adam035.networkdrive.domain.model.StorageResource.Type.FILE;

@Component
@RequiredArgsConstructor
public class DeleteStorageResourceUseCase {

    private final StorageResourceRepository storageResourceRepository;

    private final FileRepository fileRepository;

    private final DirectoryRepository directoryRepository;

    private final StoragePort storagePort;

    private final AuthUserExtractorPort authUserExtractorPort;

    private final StorageResourceAccessService storageResourceAccessService;

    @Transactional
    public void deleteStorageResource(String path) {
        StorageResource storageResource = storageResourceRepository.findByPath(String.format("/%s", path))
                .orElseThrow(StorageResourceNotFoundException::new);

        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        if (!storageResourceAccessService.canAccess(storageResource, user)) {
            throw new UnauthorizedException();
        }

        if (storageResource.getType().equals(FILE)) {
            fileRepository.findById(storageResource.getId())
                    .ifPresent(file -> deleteFiles(List.of(file)));

            return;
        }

        directoryRepository.findById(storageResource.getId())
                .ifPresent(this::deleteDirectory);
    }

    @Transactional
    protected void deleteFiles(List<File> files) {
        List<String> storageKeys = files.stream()
                .map(File::getStorageKey)
                .toList();

        List<String> ids = files.stream()
                .map(File::getStorageKey)
                .toList();

        storagePort.deleteFiles(storageKeys);
        fileRepository.deleteAllById(ids);
    }

    @Transactional
    protected void deleteDirectory(Directory directory) {
        directoryRepository.findAllByParent(directory)
                .forEach(this::deleteDirectory);

        List<File> files = fileRepository.findAllByParent(directory);
        deleteFiles(files);

        directoryRepository.deleteById(directory.getId());
    }

}
