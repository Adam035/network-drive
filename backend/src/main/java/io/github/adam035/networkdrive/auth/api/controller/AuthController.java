package io.github.adam035.networkdrive.auth.api.controller;

import io.github.adam035.networkdrive.auth.api.dto.FinishRegistrationRequest;
import io.github.adam035.networkdrive.auth.api.dto.FinishRegistrationResponse;
import io.github.adam035.networkdrive.auth.api.dto.StartRegistrationRequest;
import io.github.adam035.networkdrive.auth.api.dto.StartRegistrationResponse;
import io.github.adam035.networkdrive.auth.application.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/start")
    public StartRegistrationResponse startRegistration(@RequestBody StartRegistrationRequest startRegisterRequest) {
        return authService.startRegistration(startRegisterRequest);
    }

    @PostMapping("/register/finish")
    public FinishRegistrationResponse finishRegistration(@RequestBody FinishRegistrationRequest finishRegisterRequest) {
        return authService.finishRegistration(finishRegisterRequest);
    }

    @PostMapping("/login/start")
    public void startLogin() {
        // TODO
    }

    @PostMapping("/login/finish")
    public void finishLogin() {
        // TODO
    }

}
