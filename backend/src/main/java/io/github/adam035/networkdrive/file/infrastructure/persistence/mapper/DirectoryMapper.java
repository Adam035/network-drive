package io.github.adam035.networkdrive.file.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.file.domain.model.Directory;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.DirectoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = StorageResourceMapper.class
)
public interface DirectoryMapper {

//    @Mapping(target = "children", source = "children")
    Directory mapToModel(DirectoryEntity source);

//    @Mapping(target = "children", source = "children")
    DirectoryEntity mapToEntity(Directory source);

}
