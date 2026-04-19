package io.github.adam035.networkdrive.auth.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.github.adam035.networkdrive.auth.api.dto.FinishRegistrationRequest;
import io.github.adam035.networkdrive.auth.api.dto.FinishRegistrationResponse;
import io.github.adam035.networkdrive.auth.api.dto.StartRegistrationRequest;
import io.github.adam035.networkdrive.auth.api.dto.StartRegistrationResponse;
import io.github.adam035.networkdrive.auth.application.exception.RegistrationException;
import io.github.adam035.networkdrive.auth.application.mapper.RegistrationResultMapper;
import io.github.adam035.networkdrive.auth.application.mapper.UserCreationMapper;
import io.github.adam035.networkdrive.auth.domain.exception.UserAlreadyExistsException;
import io.github.adam035.networkdrive.auth.domain.model.User;
import io.github.adam035.networkdrive.auth.domain.repository.CredentialRepository;
import io.github.adam035.networkdrive.auth.domain.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class RegisterService {

    private static final TemporalAmount TTL = Duration.ofMinutes(2);

    private final PasskeyService passkeyService;

    private final UserService userService;

    private final JwtService jwtService;

    private final UserCreationMapper userCreationMapper;

    private final RegistrationResultMapper registrationResultMapper;

    private final CredentialRepository credentialRepository;

    public StartRegistrationResponse startRegistration(StartRegistrationRequest startRegistrationRequest) {
        log.info("Starting registration");
        User user = userCreationMapper.mapToModel(startRegistrationRequest.user());

        if (!userService.isRegistrationAllowed(user)) {
            throw new UserAlreadyExistsException(
                    String.format("User with username %s already exists", user.getUsername())
            );
        }

        try {
            String request = passkeyService.startRegistration(user).toJson();
            String jwtToken = jwtService.generateToken(user.getUsername(), Map.of("request", request), TTL);

            return new StartRegistrationResponse(jwtToken);
        } catch (JsonProcessingException e) {
            log.error("Failed to create registration request");
            throw new RegistrationException("Failed to create registration request");
        }
    }

    public FinishRegistrationResponse finishRegistration(FinishRegistrationRequest finishRegistrationRequest) {
        log.info("Finishing registration");
        String jwtToken = finishRegistrationRequest.jwtToken();
        User user = userCreationMapper.mapToModel(finishRegistrationRequest.user());

        if (!jwtService.validateToken(jwtToken, user.getUsername())) {
            throw new JwtException("Invalid or expired JWT token");
        }

        try {
            PublicKeyCredentialCreationOptions registrationRequest = PublicKeyCredentialCreationOptions.fromJson(
                    jwtService.extractClaim(jwtToken, claims -> claims.get("request", String.class))
            );

            RegistrationResult registrationResult = passkeyService.finishRegistration(
                    registrationRequest, finishRegistrationRequest.publicKeyCredential()
            );

            user.setId(new String(registrationRequest.getUser().getId().getBytes(), StandardCharsets.UTF_8));
            credentialRepository.save(registrationResultMapper.mapToCredential(user, registrationResult));

            return new FinishRegistrationResponse(
                    user, String.format("%s has been registered successfully", user.getUsername())
            );
        } catch (RegistrationFailedException | JsonProcessingException e) {
            log.error("Failed to finish registration flow", e);
            throw new RegistrationException("Failed to finish registration flow");
        }
    }

}
