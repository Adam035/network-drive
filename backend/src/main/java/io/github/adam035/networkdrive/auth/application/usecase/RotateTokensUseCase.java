package io.github.adam035.networkdrive.auth.application.usecase;

import io.github.adam035.networkdrive.auth.application.dto.AuthTokensResult;
import io.github.adam035.networkdrive.auth.application.port.in.AuthTokenPort;
import io.github.adam035.networkdrive.auth.application.port.in.JwtPort;
import io.github.adam035.networkdrive.auth.application.exception.InvalidJwtException;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RotateTokensUseCase {

    private final JwtPort jwtPort;

    private final AuthTokenPort authTokenPort;

    public AuthTokensResult rotateAuthTokens(String refreshToken) {
        String userId = jwtPort.extractClaim(refreshToken, Claims::getSubject);

        if (!jwtPort.validateToken(refreshToken, userId, "refresh")) {
            throw new InvalidJwtException("Invalid refresh token");
        }

        return authTokenPort.generateAuthTokens(userId);
    }

}
