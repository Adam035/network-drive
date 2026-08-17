package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.port.PathPort;
import io.github.adam035.desktopfs.application.port.StorageResourcePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MoveStorageResourceUseCase {

    private final StorageResourcePort storageResourcePort;

    private final PathPort pathPort;

    public void moveStorageResource(String oldPath, String newPath, boolean replaceIfExists) {
        storageResourcePort.moveStorageResource(
                pathPort.normalizePath(oldPath),
                pathPort.normalizePath(newPath),
                replaceIfExists
        );
    }

}
