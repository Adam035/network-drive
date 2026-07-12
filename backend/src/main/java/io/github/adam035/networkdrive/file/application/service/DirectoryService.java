package io.github.adam035.networkdrive.file.application.service;

import io.github.adam035.networkdrive.common.application.port.in.AuthTokenExtractorPort;
import io.github.adam035.networkdrive.file.api.dto.DirectoryCreationRequest;
import io.github.adam035.networkdrive.file.application.exception.ResourceNotFoundException;
import io.github.adam035.networkdrive.file.application.mapper.DirectoryCreationRequestMapper;
import io.github.adam035.networkdrive.file.domain.model.Directory;
import io.github.adam035.networkdrive.file.domain.model.File;
import io.github.adam035.networkdrive.file.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.file.domain.repository.FileRepository;
import io.github.adam035.networkdrive.file.domain.repository.StorageResourceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DirectoryService {

    private final AuthTokenExtractorPort authTokenExtractorPort;

    private final FileRepository fileRepository;

    private final DirectoryRepository directoryRepository;

    private final StorageResourceRepository storageResourceRepository;

    private final StorageService storageService;

    private final DirectoryCreationRequestMapper directoryCreationRequestMapper;

    @Transactional
    public Directory createDirectory(DirectoryCreationRequest directoryCreationRequest) {
        String ownerId = authTokenExtractorPort.extractSubject();
        Directory directory = directoryCreationRequestMapper.mapToModel(directoryCreationRequest, ownerId);
        Directory savedDirectory = directoryRepository.save(directory);

        if (savedDirectory.getParentId() != null) {
            directoryRepository.findById(savedDirectory.getParentId())
                    .ifPresent(parentDirectory -> {
                        parentDirectory.getChildIds().add(savedDirectory.getId());
                        directoryRepository.save(parentDirectory);
                    });
        }

        log.info("Successfully created directory: {}", savedDirectory.getId());

        return savedDirectory;
    }

    public Directory getDirectory(String id) {
        return directoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to find non-existent directory: {}", id);
                    return new ResourceNotFoundException(String.format("Directory with ID %s not found", id));
                });
    }

    @Transactional
    public void deleteDirectory(String id) {
        directoryRepository.findById(id)
                .ifPresent(directory -> {
                    List<String> childIds = directoryRepository.findAllChildIds(directory.getId());
                    List<String> ids = new ArrayList<>(childIds);
                    ids.add(id);

                    List<String> storageKeys = fileRepository.findAllById(ids).stream()
                            .map(File::getStorageKey)
                            .toList();

                    storageService.deleteFiles(storageKeys);
                    storageResourceRepository.deleteAllById(ids);

                    if (directory.getParentId() != null) {
                        directoryRepository.findById(directory.getParentId())
                                .ifPresent(parentDirectory -> {
                                    parentDirectory.getChildIds().remove(directory.getId());
                                    directoryRepository.save(parentDirectory);
                                });
                    }
                });
    }

}
