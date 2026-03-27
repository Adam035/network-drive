package io.github.adam035.networkdrive.file.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.file.domain.model.StorageResource;
import io.github.adam035.networkdrive.file.domain.repository.StorageResourceRepository;
import io.github.adam035.networkdrive.file.infrastructure.persistence.mapper.StorageResourceMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public void deleteAllById(List<String> ids) {
        storageResourceJpaRepository.deleteAllById(ids);
    }

}
