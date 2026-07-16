package io.github.adam035.networkdrive.infrastructure.spring.security.adapter;

import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.application.port.JwtPort;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUserExtractorAdapter implements AuthUserExtractorPort {

    private final JwtPort jwtPort;

    private final UserRepository userRepository;

    @Override
    public Optional<User> extractUser() {
        String authToken = SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials()
                .toString();

        if (authToken == null) {
            return Optional.empty();
        }

        String userId = jwtPort.extractClaim(authToken, Claims::getSubject);

        return userRepository.findById(userId);
    }

}
