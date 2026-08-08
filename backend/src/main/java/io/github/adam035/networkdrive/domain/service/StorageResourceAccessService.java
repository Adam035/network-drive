package io.github.adam035.networkdrive.domain.service;

import io.github.adam035.networkdrive.domain.model.StorageResource;
import io.github.adam035.networkdrive.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StorageResourceAccessService {

    public boolean canAccess(StorageResource storageResource, User user) {
        String userId = user.getId();

        log.info("Checking if user {} can access storage resource {}...", userId, storageResource.getId());

        boolean result = storageResource.getOwner().getId().equals(userId);

        log.info("Result: {}", result);

        return result;
    }

}
