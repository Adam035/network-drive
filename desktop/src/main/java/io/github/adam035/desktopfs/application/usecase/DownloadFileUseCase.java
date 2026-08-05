package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.port.FilePort;
import io.github.adam035.desktopfs.application.port.PathPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DownloadFileUseCase {

    private final FilePort filePort;

    private final PathPort pathPort;

    public byte[] downloadFile(String path, long offset, int length) {
        return filePort.downloadFile(pathPort.normalizePath(path), offset, length);
    }

}
