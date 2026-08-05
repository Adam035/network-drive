package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.port.PathPort;
import io.github.adam035.desktopfs.application.port.StorageResourcePort;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetStorageResourceUseCase {

    private final StorageResourcePort storageResourcePort;

    private final PathPort pathPort;

    public StorageResource getStorageResource(String path) {
        System.out.println();
        System.out.println(path);
        System.out.println(pathPort.normalizePath(path));
        System.out.println();

        return storageResourcePort.getStorageResource(pathPort.normalizePath(path));
    }

}