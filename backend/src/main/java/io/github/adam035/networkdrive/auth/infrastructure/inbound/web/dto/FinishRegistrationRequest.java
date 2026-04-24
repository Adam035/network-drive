package io.github.adam035.networkdrive.auth.infrastructure.inbound.web.dto;

import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;

public record FinishRegistrationRequest(
    PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> publicKeyCredential,
    String jwtToken,
    UserCreationRequest user
) {
}
