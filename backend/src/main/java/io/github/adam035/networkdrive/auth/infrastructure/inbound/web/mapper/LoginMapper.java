package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.mapper;

import io.github.adam035.networkdrive.auth.application.dto.FinishLoginCommand;
import io.github.adam035.networkdrive.auth.application.dto.FinishLoginResult;
import io.github.adam035.networkdrive.auth.application.dto.StartLoginResult;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.FinishLoginRequest;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.FinishLoginResponse;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.StartLoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = AuthTokenMapper.class
)
public interface LoginMapper {

    StartLoginResponse mapToStartLoginResponse(StartLoginResult startLoginResult);

    FinishLoginCommand mapToFinishLoginCommand(FinishLoginRequest finishLoginRequest);

    @Mapping(source = "authTokensResult", target = "authTokens")
    FinishLoginResponse mapToFinishLoginResponse(FinishLoginResult finishLoginResult);

}
