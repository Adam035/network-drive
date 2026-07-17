package io.github.adam035.networkdrive.infrastructure.spring.web.mapper;

import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.UserDetailsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    UserDetailsResponse mapToUserDetailsResponse(User user);

}
