package io.github.adam035.networkdrive.auth.application.assembler;

import com.yubico.webauthn.RegistrationResult;
import io.github.adam035.networkdrive.auth.domain.model.Credential;
import org.springframework.stereotype.Component;

@Component
public class CredentialAssembler {

    public Credential mapToCredential(String userId, RegistrationResult registrationResult) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        if (registrationResult == null) {
            throw new IllegalArgumentException("registrationResult cannot be null");
        }

        Credential credential = new Credential();
        credential.setAuthenticatorId(registrationResult.getKeyId().getId().getBytes());
        credential.setPublicKey(registrationResult.getPublicKeyCose().getBytes());
        credential.setSignatureCount(registrationResult.getSignatureCount());
        credential.setUserId(userId);
        registrationResult.isDiscoverable().ifPresent(credential::setIsDiscoverable);

        return credential;
    }

}
