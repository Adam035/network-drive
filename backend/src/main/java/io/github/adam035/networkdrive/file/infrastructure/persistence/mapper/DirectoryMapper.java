package io.github.adam035.networkdrive.file.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.file.domain.model.Directory;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.DirectoryEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class
)
public interface DirectoryMapper {

    Directory mapToModel(DirectoryEntity source);

    DirectoryEntity mapToEntity(Directory source);

}
