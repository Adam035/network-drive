package io.github.adam035.networkdrive.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.domain.model.File;
import io.github.adam035.networkdrive.infrastructure.persistence.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {

    File mapToModel(FileEntity source);

    FileEntity mapToEntity(File source);

}
