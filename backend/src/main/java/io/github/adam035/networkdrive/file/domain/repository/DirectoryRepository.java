package io.github.adam035.networkdrive.file.domain.repository;

import io.github.adam035.networkdrive.file.domain.model.Directory;

import java.util.List;
import java.util.Optional;

public interface DirectoryRepository {

    Directory save(Directory directory);

    Optional<Directory> findById(String id);

    public List<String> findAllChildIds(String id);

    void delete(String id);

}
