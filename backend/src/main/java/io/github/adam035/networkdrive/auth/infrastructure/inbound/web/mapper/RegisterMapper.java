package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.mapper;

import io.github.adam035.networkdrive.auth.application.dto.FinishRegistrationCommand;
import io.github.adam035.networkdrive.auth.application.dto.FinishRegistrationResult;
import io.github.adam035.networkdrive.auth.application.dto.StartRegistrationCommand;
import io.github.adam035.networkdrive.auth.application.dto.StartRegistrationResult;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.FinishRegistrationRequest;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.FinishRegistrationResponse;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.StartRegistrationRequest;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.StartRegistrationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    StartRegistrationCommand mapToStartRegistrationCommand(StartRegistrationRequest startRegistrationRequest);

    StartRegistrationResponse mapToStartRegistrationResponse(StartRegistrationResult startRegistrationResult);

    FinishRegistrationCommand mapToFinishRegistrationCommand(FinishRegistrationRequest finishRegistrationRequest);

    FinishRegistrationResponse mapToFinishRegistrationResponse(FinishRegistrationResult finishRegistrationResult);

}