package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.port.FilePort;
import io.github.adam035.desktopfs.application.port.PathPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadFileUseCase {

    private final FilePort filePort;

    private final PathPort pathPort;

    public void uploadFile(String path, byte[] bytes, String mimeType) {
        filePort.uploadFile(pathPort.normalizePath(path), bytes, mimeType);
    }

}
