package io.github.adam035.networkdrive.file.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.file.domain.model.User;
import io.github.adam035.networkdrive.file.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        implementationName = "storageUserMapper"
)
public interface UserMapper {

    User mapToModel(UserEntity source);

    UserEntity mapToEntity(User source);

}
