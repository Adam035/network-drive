package io.github.adam035.desktopfs.application.port;

import java.nio.file.Path;

public interface MountPort {

    void mountVolume(String volumeLabel, Path path);

}
