package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.dto.EditStorageResourceCommand;
import io.github.adam035.desktopfs.application.port.PathPort;
import io.github.adam035.desktopfs.application.port.StorageResourcePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EditStorageResourceUseCase {

    private final StorageResourcePort storageResourcePort;

    private final PathPort pathPort;

    public void editStorageResource(String path, EditStorageResourceCommand editStorageResourceCommand) {
        EditStorageResourceCommand normalizedEditStorageResourceCommand = EditStorageResourceCommand.builder()
                .path(pathPort.normalizePath(editStorageResourceCommand.path()))
                .size(editStorageResourceCommand.size())
                .owner(editStorageResourceCommand.owner())
                .type(editStorageResourceCommand.type())
                .build();

        storageResourcePort.editStorageResource(pathPort.normalizePath(path), normalizedEditStorageResourceCommand);
    }

}
