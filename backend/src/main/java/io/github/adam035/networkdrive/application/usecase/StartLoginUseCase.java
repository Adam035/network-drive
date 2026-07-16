package io.github.adam035.networkdrive.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.adam035.networkdrive.application.dto.StartLoginResult;
import io.github.adam035.networkdrive.application.port.AssertionPort;
import io.github.adam035.networkdrive.application.port.JwtPort;
import io.github.adam035.networkdrive.application.exception.LoginException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class StartLoginUseCase {

    private static final TemporalAmount TTL = Duration.ofMinutes(2);

    private final AssertionPort assertionPort;

    private final JwtPort jwtPort;

    public StartLoginResult startLogin() {
        log.info("Starting login");

        try {
            String assertionRequest = assertionPort.startAssertion().toJson();
            String jwtToken = jwtPort.generateToken("assertion", Map.of("request", assertionRequest), TTL);

            return new StartLoginResult(jwtToken);
        } catch (JsonProcessingException e) {
            throw new LoginException("Failed to create assertion request");
        }
    }

}
