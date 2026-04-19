package io.github.adam035.networkdrive.auth.api.controller;

import io.github.adam035.networkdrive.auth.api.dto.*;
import io.github.adam035.networkdrive.auth.application.service.LoginService;
import io.github.adam035.networkdrive.auth.application.service.RefreshTokenService;
import io.github.adam035.networkdrive.auth.application.service.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RegisterService registerService;

    private final LoginService loginService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register/start")
    public StartRegistrationResponse startRegistration(@RequestBody StartRegistrationRequest startRegisterRequest) {
        return registerService.startRegistration(startRegisterRequest);
    }

    @PostMapping("/register/finish")
    public FinishRegistrationResponse finishRegistration(@RequestBody FinishRegistrationRequest finishRegisterRequest) {
        return registerService.finishRegistration(finishRegisterRequest);
    }

    @PostMapping("/login/start")
    public StartLoginResponse startLogin() {
        return loginService.startLogin();
    }

    @PostMapping("/login/finish")
    public FinishLoginResponse finishLogin(@RequestBody FinishLoginRequest finishLoginRequest) {
        return loginService.finishLogin(finishLoginRequest);
    }

    @PostMapping("/refresh-access-token")
    public RefreshTokenResponse refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.refreshAccessToken(refreshTokenRequest);
    }

}
