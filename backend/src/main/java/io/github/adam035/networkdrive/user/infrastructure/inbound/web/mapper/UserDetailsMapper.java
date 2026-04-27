package io.github.adam035.networkdrive.user.infrastructure.inbound.web.mapper;

import io.github.adam035.networkdrive.common.domain.model.User;
import io.github.adam035.networkdrive.user.infrastructure.inbound.web.dto.UserDetailsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    UserDetailsResponse mapToUserDetailsResponse(User user);

}
