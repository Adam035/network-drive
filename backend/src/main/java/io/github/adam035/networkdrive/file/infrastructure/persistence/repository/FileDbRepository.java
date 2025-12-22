package io.github.adam035.networkdrive.file.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.file.domain.model.File;
import io.github.adam035.networkdrive.file.domain.repository.FileRepository;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.FileEntity;
import io.github.adam035.networkdrive.file.infrastructure.persistence.mapper.FileMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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

}
