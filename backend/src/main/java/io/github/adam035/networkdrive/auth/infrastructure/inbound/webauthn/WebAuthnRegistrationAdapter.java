package io.github.adam035.networkdrive.auth.infrastructure.inbound.webauthn;

import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.github.adam035.networkdrive.auth.application.mapper.UserIdentityMapper;
import io.github.adam035.networkdrive.auth.application.port.in.RegistrationPort;
import io.github.adam035.networkdrive.common.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class WebAuthnRegistrationAdapter implements RegistrationPort {

    private final RelyingParty relyingParty;

    private final UserIdentityMapper userIdentityMapper;

    public PublicKeyCredentialCreationOptions startRegistration(User user) {
        return relyingParty.startRegistration(
                StartRegistrationOptions.builder()
                        .user(userIdentityMapper.mapToUserIdentity(user))
                        .authenticatorSelection(AuthenticatorSelectionCriteria.builder()
                                .residentKey(ResidentKeyRequirement.REQUIRED)
                                .build()
                        )
                        .build()
        );
    }

    public RegistrationResult finishRegistration(
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

}
