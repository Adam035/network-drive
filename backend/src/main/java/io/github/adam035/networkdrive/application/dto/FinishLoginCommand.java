package io.github.adam035.networkdrive.application.dto;

import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;

public record FinishLoginCommand(
        PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> publicKeyCredential,
        String jwtToken
) {
}
