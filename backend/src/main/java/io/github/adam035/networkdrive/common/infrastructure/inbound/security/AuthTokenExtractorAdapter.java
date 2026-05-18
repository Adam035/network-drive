package io.github.adam035.networkdrive.common.infrastructure.inbound.security;

import io.github.adam035.networkdrive.auth.application.port.in.JwtPort;
import io.github.adam035.networkdrive.common.application.port.in.AuthTokenExtractorPort;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthTokenExtractorAdapter implements AuthTokenExtractorPort {

    private final JwtPort jwtPort;

    @Override
    public String extractSubject() {
        String jwtToken = SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials()
                .toString();

        if (jwtToken == null) {
            return null;
        }

        return jwtPort.extractClaim(jwtToken, Claims::getSubject);
    }

}
