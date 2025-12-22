package io.github.adam035.networkdrive.file.domain.repository;

import io.github.adam035.networkdrive.file.domain.model.File;

import java.util.Optional;

public interface FileRepository {

    File save(File file);

    Optional<File> findById(String id);

}
