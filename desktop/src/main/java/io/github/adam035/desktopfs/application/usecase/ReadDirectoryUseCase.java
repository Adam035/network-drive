package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.dto.ReadDirectoryResult;
import io.github.adam035.desktopfs.application.port.DirectoryPort;
import io.github.adam035.desktopfs.application.port.PathPort;
import io.github.adam035.desktopfs.domain.model.StorageResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadDirectoryUseCase {

    private final DirectoryPort directoryPort;

    private final PathPort pathPort;

    public ReadDirectoryResult readDirectory(String path) {
        return directoryPort.readDirectory(pathPort.normalizePath(path));
    }

}
