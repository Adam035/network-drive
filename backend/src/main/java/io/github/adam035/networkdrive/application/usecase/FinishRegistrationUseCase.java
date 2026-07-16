package io.github.adam035.networkdrive.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.github.adam035.networkdrive.application.mapper.CredentialMapper;
import io.github.adam035.networkdrive.application.dto.FinishRegistrationCommand;
import io.github.adam035.networkdrive.application.dto.FinishRegistrationResult;
import io.github.adam035.networkdrive.application.port.JwtPort;
import io.github.adam035.networkdrive.application.exception.InvalidJwtException;
import io.github.adam035.networkdrive.application.exception.RegistrationException;
import io.github.adam035.networkdrive.application.port.RegistrationPort;
import io.github.adam035.networkdrive.domain.model.Directory;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.CredentialRepository;
import io.github.adam035.networkdrive.domain.repository.DirectoryRepository;
import io.github.adam035.networkdrive.domain.repository.UserRepository;
import io.github.adam035.networkdrive.domain.service.DirectoryService;
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

    private final CredentialMapper credentialMapper;

    private final UserRepository userRepository;

    private final CredentialRepository credentialRepository;

    private final DirectoryService directoryService;

    private final DirectoryRepository directoryRepository;

    public FinishRegistrationResult finishRegistration(FinishRegistrationCommand finishRegistrationCommand) {
        log.info("Finishing registration");
        String jwtToken = finishRegistrationCommand.jwtToken();
        User user = finishRegistrationCommand.user();

        if (!jwtPort.validateToken(jwtToken, user.getUsername())) {
            throw new InvalidJwtException();
        }

        try {
            PublicKeyCredentialCreationOptions registrationRequest = PublicKeyCredentialCreationOptions.fromJson(
                    jwtPort.extractClaim(jwtToken, claims -> claims.get("request", String.class))
            );

            RegistrationResult registrationResult = registrationPort.finishRegistration(
                    registrationRequest, finishRegistrationCommand.publicKeyCredential()
            );

            user.setId(new String(registrationRequest.getUser().getId().getBytes(), StandardCharsets.UTF_8));

            userRepository.save(user);
            credentialRepository.save(credentialMapper.mapToCredential(user.getId(), registrationResult));

            Directory rootDirectory = directoryService.createDirectory(String.format("/%s", user.getUsername()), null, user);
            directoryRepository.save(rootDirectory);

            return new FinishRegistrationResult(
                    String.format("%s has been registered successfully", user.getUsername())
            );
        } catch (RegistrationFailedException | JsonProcessingException e) {
            throw new RegistrationException("Failed to finish registration flow");
        }
    }

}
