package io.github.adam035.desktopfs.application.port;

public interface PathPort {

    String normalizePath(String path, String volumeLabel);

}
