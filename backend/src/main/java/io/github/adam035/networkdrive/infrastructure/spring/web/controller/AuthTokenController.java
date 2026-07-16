package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.infrastructure.spring.web.dto.AuthTokensResponse;
import io.github.adam035.networkdrive.application.usecase.RotateTokensUseCase;
import io.github.adam035.networkdrive.infrastructure.spring.web.mapper.AuthTokenMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth/token")
public class AuthTokenController {

    private final RotateTokensUseCase rotateTokensUseCase;

    private final AuthTokenMapper authTokenMapper;

    @PostMapping("/rotate")
    public ResponseEntity<AuthTokensResponse> rotateAuthTokens(@CookieValue("refreshToken") String refreshToken) {
        AuthTokensResponse authTokensResponse = authTokenMapper.mapToAuthTokensResponse(
                rotateTokensUseCase.rotateAuthTokens(refreshToken)
        );

        ResponseCookie access = ResponseCookie.from("accessToken", authTokensResponse.accessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(5))
                .build();

        ResponseCookie refresh = ResponseCookie.from("refreshToken", authTokensResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, access.toString())
                .header(HttpHeaders.SET_COOKIE, refresh.toString())
                .body(authTokensResponse);
    }

}
