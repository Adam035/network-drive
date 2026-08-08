package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.infrastructure.persistence.entity.DirectoryEntity;
import io.github.adam035.networkdrive.infrastructure.persistence.mapper.DirectoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
class DirectoryDbRepository implements DirectoryRepository {

    private final DirectoryJpaRepository directoryJpaRepository;

    private final DirectoryMapper directoryMapper;

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
    public Optional<Directory> findByPath(String path) {
        return directoryJpaRepository.findByPath(path)
                .map(directoryMapper::mapToModel);
    }

    @Override
    public List<Directory> findAllByParent(Directory parent) {
        return directoryJpaRepository.findAllByParentId(parent.getId()).stream()
                .map(directoryMapper::mapToModel)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        directoryJpaRepository.deleteById(id);
    }

}
