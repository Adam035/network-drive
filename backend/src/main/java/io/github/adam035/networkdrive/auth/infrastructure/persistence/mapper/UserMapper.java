package io.github.adam035.networkdrive.auth.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.auth.domain.model.User;
import io.github.adam035.networkdrive.auth.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        implementationName = "authUserMapper"
)
public interface UserMapper {

    User mapToModel(UserEntity source);

    UserEntity mapToEntity(User source);

}
