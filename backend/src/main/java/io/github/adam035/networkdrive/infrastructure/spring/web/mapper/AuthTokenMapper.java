package io.github.adam035.networkdrive.infrastructure.spring.web.mapper;

import io.github.adam035.networkdrive.application.dto.AuthTokensResult;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.AuthTokensResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthTokenMapper {

    AuthTokensResponse mapToAuthTokensResponse(AuthTokensResult authTokensResult);

}
