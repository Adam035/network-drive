package io.github.adam035.networkdrive.application.dto;

import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import io.github.adam035.networkdrive.domain.model.User;

public record FinishRegistrationCommand(
        PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> publicKeyCredential,
        String jwtToken,
        User user
) {
}
