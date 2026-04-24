package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.controller;

import io.github.adam035.networkdrive.auth.application.dto.FinishLoginCommand;
import io.github.adam035.networkdrive.auth.application.usecase.FinishLoginUseCase;
import io.github.adam035.networkdrive.auth.application.usecase.StartLoginUseCase;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.FinishLoginRequest;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.FinishLoginResponse;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.StartLoginResponse;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.mapper.LoginMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth/login")
public class AuthLoginController {

    private final StartLoginUseCase startLoginUseCase;

    private final FinishLoginUseCase finishLoginUseCase;

    private final LoginMapper loginMapper;

    @PostMapping("/start")
    public StartLoginResponse startLogin() {
        return loginMapper.mapToStartLoginResponse(startLoginUseCase.startLogin());
    }

    @PostMapping("/finish")
    public FinishLoginResponse finishLogin(@RequestBody FinishLoginRequest finishLoginRequest) {
        FinishLoginCommand finishLoginCommand = loginMapper.mapToFinishLoginCommand(finishLoginRequest);
        return loginMapper.mapToFinishLoginResponse(finishLoginUseCase.finishLogin(finishLoginCommand));
    }

}
