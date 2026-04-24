package io.github.adam035.networkdrive.auth.application.port.in;

import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.exception.AssertionFailedException;

public interface AssertionPort {

    AssertionRequest startAssertion();

    AssertionResult finishAssertion(
            AssertionRequest request,
            PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> response
    ) throws AssertionFailedException;

}
