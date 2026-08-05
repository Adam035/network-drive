package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.port.PathPort;
import org.springframework.stereotype.Component;

@Component
public class NetworkDrivePathAdapter implements PathPort {

    @Override
    public String normalizePath(String path) {
        String username = "user1";

        if (path.equals("\\")) {
            return "/user1";
        }

        String normalizedPath = path.replace("\\", "/");

        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }

        if (!normalizedPath.startsWith("/user1")) {
            normalizedPath = "/user1" + normalizedPath;
        }

        if (normalizedPath.endsWith("/")) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
        }

        return normalizedPath;
    }

}
