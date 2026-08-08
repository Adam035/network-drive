package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.StorageResource;
import io.github.adam035.networkdrive.domain.repository.StorageResourceRepository;
import io.github.adam035.networkdrive.infrastructure.persistence.mapper.StorageResourceMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
class StorageResourceDbRepository implements StorageResourceRepository {

    private StorageResourceJpaRepository storageResourceJpaRepository;

    private StorageResourceMapper storageResourceMapper;

    @Override
    public List<StorageResource> findAllById(List<String> ids) {
        return storageResourceJpaRepository.findAllById(ids).stream()
                .map(storageResourceMapper::mapToModel)
                .toList();
    }

    @Override
    public List<StorageResource> findAllByParent(Directory parent) {
        return storageResourceJpaRepository.findAllByParentId(parent.getId()).stream()
                .map(storageResourceMapper::mapToModel)
                .toList();
    }

    @Override
    public Optional<StorageResource> findByPath(String path) {
        return storageResourceJpaRepository.findByPath(path)
                .map(storageResourceMapper::mapToModel);
    }

    @Override
    public void deleteAllById(List<String> ids) {
        storageResourceJpaRepository.deleteAllById(ids);
    }

}
