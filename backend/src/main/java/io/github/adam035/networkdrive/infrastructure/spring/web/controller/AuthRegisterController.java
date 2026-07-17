package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.dto.FinishRegistrationCommand;
import io.github.adam035.networkdrive.application.dto.StartRegistrationCommand;
import io.github.adam035.networkdrive.application.usecase.FinishRegistrationUseCase;
import io.github.adam035.networkdrive.application.usecase.StartRegistrationUseCase;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FinishRegistrationRequest;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FinishRegistrationResponse;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.StartRegistrationRequest;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.StartRegistrationResponse;
import io.github.adam035.networkdrive.infrastructure.spring.web.mapper.RegisterMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth/register")
public class AuthRegisterController {

    private final StartRegistrationUseCase startRegistrationUseCase;

    private final FinishRegistrationUseCase finishRegistrationUseCase;

    private final RegisterMapper registerMapper;

    @PostMapping("/start")
    public StartRegistrationResponse startRegistration(@RequestBody StartRegistrationRequest startRegistrationRequest) {
        StartRegistrationCommand startRegistrationCommand = registerMapper.mapToStartRegistrationCommand(startRegistrationRequest);
        return registerMapper.mapToStartRegistrationResponse(startRegistrationUseCase.startRegistration(startRegistrationCommand));
    }

    @PostMapping("/finish")
    public FinishRegistrationResponse finishRegistration(@RequestBody FinishRegistrationRequest finishRegistrationRequest) {
        FinishRegistrationCommand finishRegistrationCommand = registerMapper.mapToFinishRegistrationCommand(finishRegistrationRequest);
        return registerMapper.mapToFinishRegistrationResponse(finishRegistrationUseCase.finishRegistration(finishRegistrationCommand));
    }

}