package io.github.adam035.networkdrive.auth.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.exception.AssertionFailedException;
import io.github.adam035.networkdrive.auth.application.dto.FinishLoginCommand;
import io.github.adam035.networkdrive.auth.application.dto.FinishLoginResult;
import io.github.adam035.networkdrive.auth.application.port.in.AssertionPort;
import io.github.adam035.networkdrive.auth.application.port.in.AuthTokenPort;
import io.github.adam035.networkdrive.auth.application.port.in.JwtPort;
import io.github.adam035.networkdrive.auth.application.exception.LoginException;
import io.github.adam035.networkdrive.auth.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.common.domain.model.User;
import io.github.adam035.networkdrive.user.domain.repository.UserRepository;
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
                    .orElseThrow(() -> new UserDoesNotExist(
                            String.format("User with username %s does not exist", assertionResult.getUsername())
                    ));

            return new FinishLoginResult(
                    authTokenPort.generateAuthTokens(userId),
                    String.format("%s logged in successfully", assertionResult.getUsername())
            );
        } catch (JsonProcessingException | AssertionFailedException e) {
            throw new LoginException("Failed to finish login");
        }
    }

}
