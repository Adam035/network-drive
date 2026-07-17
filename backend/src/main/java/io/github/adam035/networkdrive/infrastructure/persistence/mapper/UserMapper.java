package io.github.adam035.networkdrive.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToModel(UserEntity source);

    UserEntity mapToEntity(User source);

}
