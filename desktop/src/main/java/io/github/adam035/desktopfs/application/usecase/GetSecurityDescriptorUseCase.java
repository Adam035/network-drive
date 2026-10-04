package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.port.PathPort;
import io.github.adam035.desktopfs.application.port.StorageResourcePort;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetSecurityDescriptorUseCase {

    private final StorageResourcePort storageResourcePort;

    private final PathPort pathPort;

    public Optional<byte[]> getSecurityDescriptor(String path, String volumeLabel) {
        return storageResourcePort.getStorageResource(pathPort.normalizePath(path, volumeLabel))
                .map(StorageResource::getSecurityDescriptor);
    }

}
