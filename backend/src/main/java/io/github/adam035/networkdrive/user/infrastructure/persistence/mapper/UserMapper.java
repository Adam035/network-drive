package io.github.adam035.networkdrive.user.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.user.domain.model.User;
import io.github.adam035.networkdrive.user.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        implementationName = "authUserMapper"
)
public interface UserMapper {

    User mapToModel(UserEntity source);

    UserEntity mapToEntity(User source);

}
