package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.controller;

import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.AuthTokensResponse;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.RotateTokensRequest;
import io.github.adam035.networkdrive.auth.application.usecase.RotateTokensUseCase;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.mapper.AuthTokenMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth/token")
public class AuthTokenController {

    private final RotateTokensUseCase rotateTokensUseCase;

    private final AuthTokenMapper authTokenMapper;

    @PostMapping("/rotate")
    public ResponseEntity<AuthTokensResponse> rotateAuthTokens(@RequestBody RotateTokensRequest rotateTokensRequest) {
        AuthTokensResponse authTokensResponse = authTokenMapper.mapToAuthTokensResponse(
                rotateTokensUseCase.rotateAuthTokens(rotateTokensRequest.refreshToken())
        );

        return ResponseEntity.ok()
                .header("Set-Cookie", String.format("accessToken=%s; HttpOnly; Secure;", authTokensResponse.accessToken()))
                .header("Set-Cookie", String.format("refreshToken=%s; HttpOnly; Secure;", authTokensResponse.refreshToken()))
                .body(authTokensResponse);
    }

}
