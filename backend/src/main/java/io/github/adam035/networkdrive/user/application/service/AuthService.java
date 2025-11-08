package io.github.adam035.networkdrive.user.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.github.adam035.networkdrive.user.api.dto.FinishRegistrationRequest;
import io.github.adam035.networkdrive.user.api.dto.FinishRegistrationResponse;
import io.github.adam035.networkdrive.user.api.dto.StartRegistrationRequest;
import io.github.adam035.networkdrive.user.api.dto.StartRegistrationResponse;
import io.github.adam035.networkdrive.user.application.mapper.RegistrationResultMapper;
import io.github.adam035.networkdrive.user.application.mapper.UserCreationMapper;
import io.github.adam035.networkdrive.user.application.mapper.UserIdentityMapper;
import io.github.adam035.networkdrive.user.domain.exception.UserAlreadyExistsException;
import io.github.adam035.networkdrive.user.domain.model.User;
import io.github.adam035.networkdrive.user.domain.repository.CredentialRepository;
import io.github.adam035.networkdrive.user.domain.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;

    private final JwtService jwtService;

    private final UserCreationMapper userCreationMapper;

    private final UserIdentityMapper userIdentityMapper;

    private final RegistrationResultMapper registrationResultMapper;

    private final CredentialRepository credentialRepository;

    private final RelyingParty relyingParty;

    public StartRegistrationResponse startRegistration(StartRegistrationRequest startRegistrationRequest) {
        User user = userCreationMapper.mapToModel(startRegistrationRequest.user());

        if (!userService.isRegistrationAllowed(user)) {
            throw new UserAlreadyExistsException("User already exists");
        }

        try {
            String request = relyingParty.startRegistration(
                    StartRegistrationOptions.builder()
                            .user(userIdentityMapper.mapToUserIdentity(user))
                            .build()
            )
            .toJson();

            String jwtToken = jwtService.generateToken(user.getUsername(), Map.of("request", request));

            return new StartRegistrationResponse(jwtToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public FinishRegistrationResponse finishRegistration(FinishRegistrationRequest finishRegistrationRequest) {
        String jwtToken = finishRegistrationRequest.jwtToken();
        User user = userCreationMapper.mapToModel(finishRegistrationRequest.user());

        if (!jwtService.validateToken(jwtToken, user.getUsername())) {
            throw new JwtException("Invalid or expired JWT token");
        }

        try {
            PublicKeyCredentialCreationOptions request = PublicKeyCredentialCreationOptions.fromJson(
                    jwtService.extractClaim(jwtToken, claims -> claims.get("request", String.class))
            );

            RegistrationResult result = relyingParty.finishRegistration(
                    FinishRegistrationOptions.builder()
                            .request(request)
                            .response(finishRegistrationRequest.publicKeyCredential())
                            .build()
            );

            credentialRepository.save(registrationResultMapper.mapToCredential(user, result));

            return new FinishRegistrationResponse(
                    true, String.format("%s has been registered", user.getUsername())
            );
        } catch (RegistrationFailedException e) {
            throw new IllegalStateException("WebAuthn registration failed", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
