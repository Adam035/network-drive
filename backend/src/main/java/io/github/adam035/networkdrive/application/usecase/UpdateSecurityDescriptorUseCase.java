package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.exception.UnauthorizedException;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.StorageResourceNotFoundException;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.StorageResource;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.StorageResourceRepository;
import io.github.adam035.networkdrive.domain.service.StorageResourceAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateSecurityDescriptorUseCase {

    private final AuthUserExtractorPort authUserExtractorPort;

    private final StorageResourceAccessService storageResourceAccessService;

    private final StorageResourceRepository storageResourceRepository;

    public void updateSecurityDescriptor(String path, byte[] securityDescriptor) {
        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        StorageResource storageResource = storageResourceRepository.findByPath(path)
                .orElseThrow(() -> new StorageResourceNotFoundException(path));

        if (!storageResourceAccessService.canAccess(storageResource, user)) {
            throw new UnauthorizedException();
        }

        storageResource.setSecurityDescriptor(securityDescriptor);
        storageResourceRepository.save(storageResource);
    }

}
