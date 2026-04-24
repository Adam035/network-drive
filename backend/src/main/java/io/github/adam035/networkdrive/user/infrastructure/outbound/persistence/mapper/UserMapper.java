package io.github.adam035.networkdrive.user.infrastructure.outbound.persistence.mapper;

import io.github.adam035.networkdrive.common.domain.model.User;
import io.github.adam035.networkdrive.user.infrastructure.outbound.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToModel(UserEntity source);

    UserEntity mapToEntity(User source);

}
