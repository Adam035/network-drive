package io.github.adam035.networkdrive.domain.repository;

import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {

    File save(File file);

    Optional<File> findById(String id);

    Optional<File> findByPath(String path);

    List<File> findAllById(List<String> ids);

    List<File> findAllByParent(Directory parent);

    Optional<File> deleteById(String id);

    void deleteAllById(List<String> ids);

}
