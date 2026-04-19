package io.github.adam035.networkdrive.auth.application.service;

import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.github.adam035.networkdrive.auth.application.mapper.UserIdentityMapper;
import io.github.adam035.networkdrive.auth.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
class PasskeyService {

    private final RelyingParty relyingParty;

    private final UserIdentityMapper userIdentityMapper;

    PublicKeyCredentialCreationOptions startRegistration(User user) {
        return relyingParty.startRegistration(
                StartRegistrationOptions.builder()
                        .user(userIdentityMapper.mapToUserIdentity(user))
                        .authenticatorSelection(AuthenticatorSelectionCriteria.builder()
                                .residentKey(ResidentKeyRequirement.REQUIRED)
                                .build()
                        ).build()
        );
    }

    RegistrationResult finishRegistration(
            PublicKeyCredentialCreationOptions request,
            PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> response
    ) throws RegistrationFailedException {
        return relyingParty.finishRegistration(
                FinishRegistrationOptions.builder()
                        .request(request)
                        .response(response)
                        .build()
        );
    }

    AssertionRequest startAssertion() {
        return relyingParty.startAssertion(StartAssertionOptions.builder().build());
    }

    AssertionResult finishAssertion(
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
