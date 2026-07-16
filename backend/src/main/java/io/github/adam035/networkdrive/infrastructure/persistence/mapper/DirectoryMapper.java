package io.github.adam035.networkdrive.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.infrastructure.persistence.entity.DirectoryEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class
)
public interface DirectoryMapper {

    Directory mapToModel(DirectoryEntity source);

    DirectoryEntity mapToEntity(Directory source);

}
