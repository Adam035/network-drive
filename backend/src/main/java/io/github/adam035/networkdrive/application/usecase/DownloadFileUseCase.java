package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.FileDownloadResult;
import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.application.port.StoragePort;
import io.github.adam035.networkdrive.domain.exception.StorageResourceNotFoundException;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.File;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.FileRepository;
import io.github.adam035.networkdrive.domain.service.StorageResourceAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class DownloadFileUseCase {

    private final AuthUserExtractorPort authUserExtractorPort;

    private final StorageResourceAccessService storageResourceAccessService;

    private final FileRepository fileRepository;

    private final StoragePort storagePort;

    public FileDownloadResult downloadFile(String path) {
        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        File file = fileRepository.findByPath(path)
                .orElseThrow(StorageResourceNotFoundException::new);

        if (!storageResourceAccessService.canAccess(file, user)) {
            throw new UnauthorizedException();
        }

        InputStream fileContent = storagePort.downloadFile(file.getStorageKey());

        return new FileDownloadResult(file, fileContent);
    }

}
