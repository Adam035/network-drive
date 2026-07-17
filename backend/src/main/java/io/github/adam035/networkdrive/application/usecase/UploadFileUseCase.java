package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.exception.FileUploadException;
import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.mapper.FileUploadMapper;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.application.port.StoragePort;
import io.github.adam035.networkdrive.domain.exception.StorageResourceNotFoundException;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.File;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.domain.repository.FileRepository;
import io.github.adam035.networkdrive.domain.service.DirectoryService;
import io.github.adam035.networkdrive.domain.service.StorageResourceAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UploadFileUseCase {

    private final FileRepository fileRepository;

    private final DirectoryRepository directoryRepository;

    private final DirectoryService directoryService;

    private final AuthUserExtractorPort authUserExtractorPort;

    private final StorageResourceAccessService storageResourceAccessService;

    private final FileUploadMapper fileUploadMapper;

    private final StoragePort storagePort;

    @Transactional
    public File uploadFile(MultipartFile multipartFile, String path) {
        Directory parentDirectory = directoryService.findParentDirectoryByPath(path)
                .orElseThrow(StorageResourceNotFoundException::new);

        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        if (!storageResourceAccessService.canAccess(parentDirectory, user)) {
            throw new UnauthorizedException();
        }

        File file = fileUploadMapper.mapToModel(multipartFile, path, user);

        try {
            storagePort.uploadFile(file, multipartFile.getInputStream());
        } catch (IOException e) {
            throw new FileUploadException();
        }

        directoryService.addStorageResource(parentDirectory, file);
        directoryRepository.save(parentDirectory);

        return fileRepository.save(file);
    }

}
