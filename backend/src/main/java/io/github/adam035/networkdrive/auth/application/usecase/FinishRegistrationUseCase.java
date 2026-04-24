package io.github.adam035.networkdrive.auth.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.github.adam035.networkdrive.auth.application.assembler.CredentialAssembler;
import io.github.adam035.networkdrive.auth.application.dto.FinishRegistrationCommand;
import io.github.adam035.networkdrive.auth.application.dto.FinishRegistrationResult;
import io.github.adam035.networkdrive.auth.application.port.in.JwtPort;
import io.github.adam035.networkdrive.auth.application.port.out.UserRepositoryPort;
import io.github.adam035.networkdrive.auth.application.exception.InvalidJwtException;
import io.github.adam035.networkdrive.auth.application.exception.RegistrationException;
import io.github.adam035.networkdrive.auth.application.port.in.RegistrationPort;
import io.github.adam035.networkdrive.common.domain.model.User;
import io.github.adam035.networkdrive.auth.domain.repository.CredentialRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@AllArgsConstructor
public class FinishRegistrationUseCase {

    private final RegistrationPort registrationPort;

    private final JwtPort jwtPort;

    private final CredentialAssembler credentialAssembler;

    private final UserRepositoryPort userRepositoryPort;

    private final CredentialRepository credentialRepository;

    public FinishRegistrationResult finishRegistration(FinishRegistrationCommand finishRegistrationCommand) {
        log.info("Finishing registration");
        String jwtToken = finishRegistrationCommand.jwtToken();
        User user = finishRegistrationCommand.user();

        if (!jwtPort.validateToken(jwtToken, user.getUsername())) {
            throw new InvalidJwtException("Invalid or expired JWT token");
        }

        try {
            PublicKeyCredentialCreationOptions registrationRequest = PublicKeyCredentialCreationOptions.fromJson(
                    jwtPort.extractClaim(jwtToken, claims -> claims.get("request", String.class))
            );

            RegistrationResult registrationResult = registrationPort.finishRegistration(
                    registrationRequest, finishRegistrationCommand.publicKeyCredential()
            );

            user.setId(new String(registrationRequest.getUser().getId().getBytes(), StandardCharsets.UTF_8));

            userRepositoryPort.save(user);
            credentialRepository.save(credentialAssembler.mapToCredential(user.getId(), registrationResult));

            return new FinishRegistrationResult(
                    String.format("%s has been registered successfully", user.getUsername())
            );
        } catch (RegistrationFailedException | JsonProcessingException e) {
            throw new RegistrationException("Failed to finish registration flow");
        }
    }

}
