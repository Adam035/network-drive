package io.github.adam035.networkdrive.file.domain.repository;

import io.github.adam035.networkdrive.file.domain.model.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {

    File save(File file);

    Optional<File> findById(String id);

    List<File> findAllById(List<String> ids);

    Optional<File> deleteById(String id);

}
