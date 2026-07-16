package io.github.adam035.networkdrive.infrastructure.spring.web.mapper;

import io.github.adam035.networkdrive.application.dto.FinishLoginCommand;
import io.github.adam035.networkdrive.application.dto.FinishLoginResult;
import io.github.adam035.networkdrive.application.dto.StartLoginResult;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FinishLoginRequest;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FinishLoginResponse;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.StartLoginResponse;
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
