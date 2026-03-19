package io.github.adam035.networkdrive.file.domain.repository;

import io.github.adam035.networkdrive.file.domain.model.StorageResource;

import java.util.List;

public interface StorageResourceRepository {

    public List<StorageResource> findAllById(List<String> ids);

    public void deleteAllById(List<String> ids);

}
