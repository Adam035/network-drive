package io.github.adam035.networkdrive.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.exception.AssertionFailedException;
import io.github.adam035.networkdrive.application.dto.FinishLoginCommand;
import io.github.adam035.networkdrive.application.dto.FinishLoginResult;
import io.github.adam035.networkdrive.application.port.AssertionPort;
import io.github.adam035.networkdrive.application.port.AuthTokenPort;
import io.github.adam035.networkdrive.application.port.JwtPort;
import io.github.adam035.networkdrive.application.exception.LoginException;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FinishLoginUseCase {

    private final AssertionPort assertionPort;

    private final JwtPort jwtPort;

    private final AuthTokenPort authTokenPort;

    private final UserRepository userRepository;

    public FinishLoginResult finishLogin(FinishLoginCommand finishLoginCommand) {
        log.info("Finishing login");
        String jwtToken = finishLoginCommand.jwtToken();

        if (!jwtPort.validateToken(jwtToken, "assertion")) {
            throw new JwtException("Invalid or expired JWT token");
        }

        try {
            AssertionRequest request = AssertionRequest.fromJson(
                    jwtPort.extractClaim(jwtToken, claims -> claims.get("request", String.class))
            );

            AssertionResult assertionResult = assertionPort.finishAssertion(
                    request, finishLoginCommand.publicKeyCredential()
            );

            String userId = userRepository.findByUsername(assertionResult.getUsername())
                    .map(User::getId)
                    .orElseThrow(UserDoesNotExist::new);

            return new FinishLoginResult(
                    authTokenPort.generateAuthTokens(userId),
                    String.format("%s logged in successfully", assertionResult.getUsername())
            );
        } catch (JsonProcessingException | AssertionFailedException e) {
            throw new LoginException("Failed to finish login");
        }
    }

}
