package io.github.adam035.networkdrive.domain.service;

import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.StorageResource;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DirectoryService {

    private final DirectoryRepository directoryRepository;

    public Directory createDirectory(String path, Directory parentDirectory, User owner) {
        String name = path.substring(path.lastIndexOf('/') + 1);

        Directory directory = new Directory();
        directory.setName(name);
        directory.setPath(path);
        directory.setSize(0L);
        directory.setParentId(parentDirectory != null ? parentDirectory.getId() : null);
        directory.setOwner(owner);

        return directory;
    }

    public Optional<Directory> findParentDirectoryByPath(String path) {
        String parentDirectoryPath = path.substring(0, path.lastIndexOf('/'));
        return directoryRepository.findByPath(parentDirectoryPath);
    }

    @Transactional
    public void addStorageResource(Directory directory, StorageResource storageResource) {
        long size = storageResource.getSize() == null ? 0L : storageResource.getSize();
        String path = directory.getPath().concat("/").concat(storageResource.getName());

        storageResource.setParentId(directory.getId());
        storageResource.setPath(path);

        updateSize(directory, size);
    }

    @Transactional
    public void updateSize(Directory directory, long size) {
        Directory currentDirectory = directory;

        do {
            currentDirectory.setSize(currentDirectory.getSize() + size);
            directoryRepository.save(currentDirectory);

            if (currentDirectory.getParentId() == null) {
                break;
            }

            currentDirectory = directoryRepository.findById(currentDirectory.getParentId())
                    .orElse(null);
        } while (currentDirectory != null);
    }

}
