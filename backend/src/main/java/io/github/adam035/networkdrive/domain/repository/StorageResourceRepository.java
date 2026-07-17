package io.github.adam035.networkdrive.domain.repository;

import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.StorageResource;

import java.util.List;
import java.util.Optional;

public interface StorageResourceRepository {

    List<StorageResource> findAllById(List<String> ids);

    List<StorageResource> findAllByParent(Directory parent);

    Optional<StorageResource> findByPath(String path);

    void deleteAllById(List<String> ids);

}
