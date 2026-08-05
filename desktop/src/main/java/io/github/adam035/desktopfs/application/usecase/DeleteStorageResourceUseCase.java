package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.port.PathPort;
import io.github.adam035.desktopfs.application.port.StorageResourcePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteStorageResourceUseCase {

    private final StorageResourcePort storageResourcePort;

    private final PathPort pathPort;

    public void deleteStorageResource(String path) {
        storageResourcePort.deleteStorageResource(pathPort.normalizePath(path));
    }

}
