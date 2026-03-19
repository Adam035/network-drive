package io.github.adam035.networkdrive.file.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.file.domain.model.Directory;
import io.github.adam035.networkdrive.file.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.file.domain.repository.StorageResourceRepository;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.DirectoryEntity;
import io.github.adam035.networkdrive.file.infrastructure.persistence.mapper.DirectoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
class DirectoryDbRepository implements DirectoryRepository {

    private DirectoryJpaRepository directoryJpaRepository;

    private DirectoryMapper directoryMapper;

    private StorageResourceRepository storageResourceRepository;

    @Override
    public Directory save(Directory directory) {
        DirectoryEntity directoryEntity = directoryMapper.mapToEntity(directory);
        return directoryMapper.mapToModel(directoryJpaRepository.save(directoryEntity));
    }

    @Override
    public Optional<Directory> findById(String id) {
        return directoryJpaRepository.findById(id)
                .map(directoryMapper::mapToModel);
    }

    @Override
    public List<String> findAllChildIds(String id) {
        return directoryJpaRepository.findAllChildIds(id);
    }

    @Override
    @Transactional
    public void delete(String id) {
        directoryJpaRepository.findById(id)
                .ifPresent(directory -> {
                    List<String> ids = findAllChildIds(id);
                    ids.add(id);
                    storageResourceRepository.deleteAllById(ids);

                    if (directory.getParentId() != null) {
                        directoryJpaRepository.findById(directory.getParentId())
                                .ifPresent(parentDirectory -> {
                                    parentDirectory.getChildIds().remove(id);
                                    directoryJpaRepository.save(parentDirectory);
                                });
                    }
                });
    }



}
