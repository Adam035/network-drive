package io.github.adam035.networkdrive.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.adam035.networkdrive.application.dto.StartRegistrationCommand;
import io.github.adam035.networkdrive.application.dto.StartRegistrationResult;
import io.github.adam035.networkdrive.application.port.JwtPort;
import io.github.adam035.networkdrive.application.exception.RegistrationException;
import io.github.adam035.networkdrive.application.port.RegistrationPort;
import io.github.adam035.networkdrive.domain.exception.UserAlreadyExistsException;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class StartRegistrationUseCase {

    private static final TemporalAmount TTL = Duration.ofMinutes(2);

    private final UserService userService;

    private final RegistrationPort registrationPort;

    private final JwtPort jwtPort;

    public StartRegistrationResult startRegistration(StartRegistrationCommand startRegistrationCommand) {
        log.info("Starting registration");
        User user = startRegistrationCommand.user();
        user.setId(UUID.randomUUID().toString());

        if (!userService.isRegistrationAllowed(user)) {
            throw new UserAlreadyExistsException(
                    String.format("User with username %s already exists", user.getUsername())
            );
        }

        try {
            String request = registrationPort.startRegistration(user).toJson();
            String jwtToken = jwtPort.generateToken(user.getUsername(), Map.of("request", request), TTL);

            return new StartRegistrationResult(jwtToken);
        } catch (JsonProcessingException e) {
            throw new RegistrationException("Failed to create registration request");
        }
    }

}
