package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.File;
import io.github.adam035.networkdrive.domain.repository.FileRepository;
import io.github.adam035.networkdrive.infrastructure.persistence.mapper.FileMapper;
import io.github.adam035.networkdrive.infrastructure.persistence.entity.FileEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class FileDbRepository implements FileRepository {

    private FileJpaRepository fileJpaRepository;

    private FileMapper fileMapper;

    @Override
    public File save(File file) {
        FileEntity fileEntity = fileMapper.mapToEntity(file);
        return fileMapper.mapToModel(fileJpaRepository.save(fileEntity));
    }

    @Override
    public Optional<File> findById(String id) {
        return fileJpaRepository.findById(id)
                .map(fileMapper::mapToModel);
    }

    @Override
    public Optional<File> findByPath(String path) {
        return fileJpaRepository.findByPath(path)
                .map(fileMapper::mapToModel);
    }

    @Override
    public List<File> findAllById(List<String> ids) {
        return fileJpaRepository.findAllById(ids).stream()
                .map(fileMapper::mapToModel)
                .toList();
    }

    @Override
    public List<File> findAllByParent(Directory parent) {
        return fileJpaRepository.findAllByParentId(parent.getId()).stream()
                .map(fileMapper::mapToModel)
                .toList();
    }

    @Override
    public Optional<File> deleteById(String id) {
        Optional<File> optionalFile = findById(id);
        optionalFile.ifPresent(file -> fileJpaRepository.deleteById(file.getId()));

        return optionalFile;
    }

    @Override
    public void deleteAllById(List<String> ids) {
        fileJpaRepository.deleteAllById(ids);
    }

}
