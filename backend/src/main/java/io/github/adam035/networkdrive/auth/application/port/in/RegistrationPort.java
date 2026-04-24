package io.github.adam035.networkdrive.auth.application.port.in;

import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.github.adam035.networkdrive.common.domain.model.User;

public interface RegistrationPort {

    PublicKeyCredentialCreationOptions startRegistration(User user);

    RegistrationResult finishRegistration(
            PublicKeyCredentialCreationOptions request,
            PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> response
    ) throws RegistrationFailedException;

}
