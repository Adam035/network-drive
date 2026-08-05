package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.exception.FileContentReadException;
import io.github.adam035.desktopfs.application.port.FilePort;
import io.github.adam035.desktopfs.application.port.PathPort;
import io.github.adam035.desktopfs.domain.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class UploadFileUseCase {

    private final FilePort filePort;

    private final PathPort pathPort;

    public void uploadFile(String path, byte[] bytes, String mimeType) {
        filePort.uploadFile(pathPort.normalizePath(path), bytes, mimeType);
    }

}
