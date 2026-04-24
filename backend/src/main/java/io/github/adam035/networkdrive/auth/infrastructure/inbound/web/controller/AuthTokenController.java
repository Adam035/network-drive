package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.controller;

import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.AuthTokensResponse;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto.RotateTokensRequest;
import io.github.adam035.networkdrive.auth.application.usecase.RotateTokensUseCase;
import io.github.adam035.networkdrive.auth.infrastructure.inbound.web.mapper.AuthTokenMapper;
import lombok.AllArgsConstructor;
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
    public AuthTokensResponse rotateAuthTokens(@RequestBody RotateTokensRequest rotateTokensRequest) {
        String refreshToken = rotateTokensRequest.refreshToken();
        return authTokenMapper.mapToAuthTokensResponse(rotateTokensUseCase.rotateAuthTokens(refreshToken));
    }

}
