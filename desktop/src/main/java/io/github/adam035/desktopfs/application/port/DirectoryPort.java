package io.github.adam035.desktopfs.application.port;

import io.github.adam035.desktopfs.application.dto.ReadDirectoryResult;
import io.github.adam035.desktopfs.domain.model.Directory;

public interface DirectoryPort {

    Directory createDirectory(String path);

    ReadDirectoryResult readDirectory(String path);

}
