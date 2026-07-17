package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.dto.FinishLoginCommand;
import io.github.adam035.networkdrive.application.usecase.FinishLoginUseCase;
import io.github.adam035.networkdrive.application.usecase.StartLoginUseCase;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FinishLoginRequest;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.FinishLoginResponse;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.StartLoginResponse;
import io.github.adam035.networkdrive.infrastructure.spring.web.mapper.LoginMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

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
    public ResponseEntity<FinishLoginResponse> finishLogin(@RequestBody FinishLoginRequest finishLoginRequest) {
        FinishLoginCommand finishLoginCommand = loginMapper.mapToFinishLoginCommand(finishLoginRequest);
        FinishLoginResponse finishLoginResponse = loginMapper.mapToFinishLoginResponse(
                finishLoginUseCase.finishLogin(finishLoginCommand)
        );

        ResponseCookie access = ResponseCookie.from("accessToken", finishLoginResponse.authTokens().accessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(5))
                .build();

        ResponseCookie refresh = ResponseCookie.from("refreshToken", finishLoginResponse.authTokens().refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, access.toString())
                .header(HttpHeaders.SET_COOKIE, refresh.toString())
                .body(finishLoginResponse);
    }

}
