package io.github.adam035.networkdrive.auth.infrastructure.inbound.webauthn;

import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import io.github.adam035.networkdrive.auth.application.port.in.AssertionPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
class WebAuthnAssertionAdapter implements AssertionPort {

    private final RelyingParty relyingParty;

    public AssertionRequest startAssertion() {
        return relyingParty.startAssertion(StartAssertionOptions.builder().build());
    }

    public AssertionResult finishAssertion(
            AssertionRequest request,
            PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> response
    ) throws AssertionFailedException {
        return relyingParty.finishAssertion(
                FinishAssertionOptions.builder()
                        .request(request)
                        .response(response)
                        .build()
        );
    }

}