package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.VolumeResult;
import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.mapper.VolumeResultMapper;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.StorageResourceNotFoundException;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.domain.service.StorageResourceAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetVolumeUseCase {

    private final AuthUserExtractorPort authUserExtractorPort;

    private final DirectoryRepository directoryRepository;

    private final StorageResourceAccessService storageResourceAccessService;

    private final VolumeResultMapper volumeResultMapper;

    public VolumeResult getVolume(String volumeLabel) {
        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        String path = "/".concat(volumeLabel);

        Directory rootDirectory = directoryRepository.findByPath(path)
                .orElseThrow(() -> new StorageResourceNotFoundException(path));

        if (!storageResourceAccessService.canAccess(rootDirectory, user)) {
            throw new UnauthorizedException();
        }

        return volumeResultMapper.mapToVolumeResult(rootDirectory, user.getAccountTier());
    }

}
