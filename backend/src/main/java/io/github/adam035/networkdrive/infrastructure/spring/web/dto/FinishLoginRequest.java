package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;

public record FinishLoginRequest(
    PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> publicKeyCredential,
    String jwtToken
) {
}
