package io.github.adam035.networkdrive.auth.application.service;

import io.github.adam035.networkdrive.auth.api.dto.AuthTokensResponse;
import io.github.adam035.networkdrive.auth.api.dto.RotateTokensRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class AuthTokenService {

    private static final TemporalAmount ACCESS_TOKEN_TTL = Duration.ofMinutes(5);

    private static final TemporalAmount REFRESH_TOKEN_TTL = Duration.ofDays(1);

    private final JwtService jwtService;

    public AuthTokensResponse generateTokens(String userId) {
        String accessToken = jwtService.generateToken(userId, Map.of("token_type", "access"), ACCESS_TOKEN_TTL);
        String refreshToken = jwtService.generateToken(userId, Map.of("token_type", "refresh"), REFRESH_TOKEN_TTL);

        return new AuthTokensResponse(accessToken, refreshToken);
    }

    public AuthTokensResponse rotateTokens(RotateTokensRequest rotateTokensRequest) {
        String refreshToken = rotateTokensRequest.refreshToken();
        String userId = jwtService.extractClaim(refreshToken, Claims::getSubject);

        if (!jwtService.validateToken(refreshToken, userId, "refresh")) {
            throw new JwtException("Invalid refresh token");
        }

        return generateTokens(rotateTokensRequest.userId());
    }

}
