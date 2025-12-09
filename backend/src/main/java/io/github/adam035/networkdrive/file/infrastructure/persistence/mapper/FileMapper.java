package io.github.adam035.networkdrive.file.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.file.domain.model.File;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = StorageResourceMapper.class
)
public interface FileMapper {

    File mapToModel(FileEntity source);

    FileEntity mapToEntity(File source);

}
