package io.github.adam035.networkdrive.domain.service;

import io.github.adam035.networkdrive.application.port.StoragePort;
import io.github.adam035.networkdrive.domain.exception.StorageResourceNotFoundException;
import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.File;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.domain.repository.FileRepository;
import io.github.adam035.networkdrive.domain.repository.StorageResourceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DirectoryService {

    private final FileRepository fileRepository;

    private final DirectoryRepository directoryRepository;

    private final StorageResourceRepository storageResourceRepository;

    private final StoragePort storageService;

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

    public void addFile(Directory directory, File file) {
        directory.setSize(directory.getSize() + file.getSize());
        file.setParentId(directory.getId());
    }

}
