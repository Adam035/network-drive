package io.github.adam035.networkdrive.infrastructure.spring.web.mapper;

import io.github.adam035.networkdrive.application.dto.FinishRegistrationCommand;
import io.github.adam035.networkdrive.application.dto.FinishRegistrationResult;
import io.github.adam035.networkdrive.application.dto.StartRegistrationCommand;
import io.github.adam035.networkdrive.application.dto.StartRegistrationResult;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FinishRegistrationRequest;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FinishRegistrationResponse;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.StartRegistrationRequest;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.StartRegistrationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    StartRegistrationCommand mapToStartRegistrationCommand(StartRegistrationRequest startRegistrationRequest);

    StartRegistrationResponse mapToStartRegistrationResponse(StartRegistrationResult startRegistrationResult);

    FinishRegistrationCommand mapToFinishRegistrationCommand(FinishRegistrationRequest finishRegistrationRequest);

    FinishRegistrationResponse mapToFinishRegistrationResponse(FinishRegistrationResult finishRegistrationResult);

}