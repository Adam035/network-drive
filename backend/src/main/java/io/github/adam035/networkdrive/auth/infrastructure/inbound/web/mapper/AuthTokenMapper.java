package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.mapper;

import io.github.adam035.networkdrive.auth.application.dto.AuthTokensResult;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.AuthTokensResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthTokenMapper {

    AuthTokensResponse mapToAuthTokensResponse(AuthTokensResult authTokensResult);

}
