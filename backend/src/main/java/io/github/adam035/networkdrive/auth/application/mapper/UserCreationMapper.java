package io.github.adam035.networkdrive.auth.application.mapper;

import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.UserCreationRequest;
import io.github.adam035.networkdrive.common.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCreationMapper {

    User mapToModel(UserCreationRequest userCreationRequest);

}
