package io.github.adam035.networkdrive.user.application.mapper;

import io.github.adam035.networkdrive.user.api.dto.UserCreationRequest;
import io.github.adam035.networkdrive.user.domain.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserCreationMapper {

    User mapToModel(UserCreationRequest source);

    @AfterMapping
    default void enrichUser(@MappingTarget User target) {
        if (target.getId() == null) {
            target.setId(UUID.randomUUID().toString());
        }
    }

}
