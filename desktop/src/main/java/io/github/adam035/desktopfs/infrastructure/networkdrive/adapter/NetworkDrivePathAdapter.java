package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.port.PathPort;
import org.springframework.stereotype.Component;

@Component
public class NetworkDrivePathAdapter implements PathPort {

    @Override
    public String normalizePath(String path, String volumeLabel) {
        String normalizedPath = path.replace("\\", "/");

        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/".concat(normalizedPath);
        }

        if (!normalizedPath.startsWith("/".concat(volumeLabel))) {
            normalizedPath = "/".concat(volumeLabel).concat(normalizedPath);
        }

        if (normalizedPath.endsWith("/")) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
        }

        return normalizedPath;
    }

}
