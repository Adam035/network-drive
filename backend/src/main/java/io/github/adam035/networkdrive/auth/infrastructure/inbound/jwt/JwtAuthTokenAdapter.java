package io.github.adam035.networkdrive.auth.infrastructure.inbound.jwt;

import io.github.adam035.networkdrive.auth.application.dto.AuthTokensResult;
import io.github.adam035.networkdrive.auth.application.port.in.AuthTokenPort;
import io.github.adam035.networkdrive.auth.application.port.in.JwtPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class JwtAuthTokenAdapter implements AuthTokenPort {

    private static final TemporalAmount ACCESS_TOKEN_TTL = Duration.ofMinutes(5);

    private static final TemporalAmount REFRESH_TOKEN_TTL = Duration.ofDays(1);

    private final JwtPort jwtPort;

    public AuthTokensResult generateAuthTokens(String userId) {
        String accessToken = jwtPort.generateToken(userId, Map.of("token_type", "access"), ACCESS_TOKEN_TTL);
        String refreshToken = jwtPort.generateToken(userId, Map.of("token_type", "refresh"), REFRESH_TOKEN_TTL);

        return new AuthTokensResult(accessToken, refreshToken);
    }

}
