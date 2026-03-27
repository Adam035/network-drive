package io.github.adam035.networkdrive.file.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.file.domain.model.File;
import io.github.adam035.networkdrive.file.domain.repository.FileRepository;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.FileEntity;
import io.github.adam035.networkdrive.file.infrastructure.persistence.mapper.FileMapper;
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
    public List<File> findAllById(List<String> ids) {
        return fileJpaRepository.findAllById(ids).stream()
                .map(fileMapper::mapToModel)
                .toList();
    }

    @Override
    public Optional<File> deleteById(String id) {
        Optional<File> optionalFile = findById(id);
        optionalFile.ifPresent(file -> fileJpaRepository.deleteById(file.getId()));

        return optionalFile;
    }

}
