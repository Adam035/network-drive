package io.github.adam035.networkdrive.infrastructure.spring.web.mapper;

import io.github.adam035.networkdrive.application.dto.UpdateUserCommand;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.UpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UpdateUserMapper {

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "updateUserRequest.username", target = "username")
    @Mapping(source = "updateUserRequest.email", target = "email")
    @Mapping(source = "updateUserRequest.accountTierName", target = "accountTierName")
    UpdateUserCommand mapToUpdateUserCommand(String userId, UpdateUserRequest updateUserRequest);

}
