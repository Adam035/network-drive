package io.github.adam035.networkdrive.application.mapper;

import io.github.adam035.networkdrive.infrastructure.spring.web.dto.UserCreationRequest;
import io.github.adam035.networkdrive.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCreationMapper {

    User mapToModel(UserCreationRequest userCreationRequest);

}
