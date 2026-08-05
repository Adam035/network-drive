package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.port.DirectoryPort;
import io.github.adam035.desktopfs.application.port.PathPort;
import io.github.adam035.desktopfs.domain.model.Directory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateDirectoryUseCase {

    private final DirectoryPort directoryPort;

    private final PathPort pathPort;

    public Directory createDirectory(String path) {
        return directoryPort.createDirectory(pathPort.normalizePath(path));
    }


}
