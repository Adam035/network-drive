package io.github.adam035.networkdrive.domain.repository;

import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface DirectoryRepository {

    Directory save(Directory directory);

    Optional<Directory> findById(String id);

    Optional<Directory> findByPath(String path);

    List<Directory> findAllByParent(Directory parent);

    void deleteById(String id);

}
