package io.github.adam035.networkdrive.auth.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import io.github.adam035.networkdrive.auth.api.dto.FinishLoginRequest;
import io.github.adam035.networkdrive.auth.api.dto.FinishLoginResponse;
import io.github.adam035.networkdrive.auth.api.dto.StartLoginResponse;
import io.github.adam035.networkdrive.auth.application.exception.LoginException;
import io.github.adam035.networkdrive.auth.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.auth.domain.model.User;
import io.github.adam035.networkdrive.auth.domain.repository.UserRepository;
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
public class LoginService {

    private static final TemporalAmount TTL = Duration.ofMinutes(2);

    private final PasskeyService passkeyService;

    private final JwtService jwtService;

    private final AuthTokenService authTokenService;

    private final UserRepository userRepository;

    public StartLoginResponse startLogin() {
        log.info("Starting login");

        try {
            String assertionRequest = passkeyService.startAssertion().toJson();
            String jwtToken = jwtService.generateToken("assertion", Map.of("request", assertionRequest), TTL);

            return new StartLoginResponse(jwtToken);
        } catch (JsonProcessingException e) {
            log.warn("Failed to create assertion request", e);
            throw new LoginException("Failed to create assertion request");
        }
    }

    public FinishLoginResponse finishLogin(FinishLoginRequest finishLoginRequest) {
        log.info("Finishing login");
        String jwtToken = finishLoginRequest.jwtToken();

        if (!jwtService.validateToken(jwtToken, "assertion")) {
            throw new JwtException("Invalid or expired JWT token");
        }

        try {
            AssertionRequest request = AssertionRequest.fromJson(
                    jwtService.extractClaim(jwtToken, claims -> claims.get("request", String.class))
            );

            AssertionResult assertionResult = passkeyService.finishAssertion(
                    request, finishLoginRequest.publicKeyCredential()
            );

            String userId = userRepository.findByUsername(assertionResult.getUsername())
                    .map(User::getId)
                    .orElseThrow(() -> new UserDoesNotExist(
                            String.format("User with username %s does not exist", assertionResult.getUsername())
                    ));

            return new FinishLoginResponse(
                    authTokenService.generateTokens(userId),
                    String.format("%s logged in successfully", assertionResult.getUsername())
            );
        } catch (JsonProcessingException | AssertionFailedException e) {
            log.warn("Failed to finish login flow", e);
            throw new LoginException("Failed to finish login flow");
        }
    }

}
