package io.github.adam035.networkdrive.auth.api.dto;

import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;

public record FinishRegistrationRequest(
    PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> publicKeyCredential,
    String jwtToken,
    UserCreationRequest user
    ) {
}
