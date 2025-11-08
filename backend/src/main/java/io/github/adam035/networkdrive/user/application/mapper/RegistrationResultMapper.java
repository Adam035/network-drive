package io.github.adam035.networkdrive.user.application.mapper;

import com.yubico.webauthn.RegistrationResult;
import io.github.adam035.networkdrive.user.domain.model.Credential;
import io.github.adam035.networkdrive.user.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationResultMapper {

    default Credential mapToCredential(User user, RegistrationResult result) {
        Credential credential = new Credential();
        credential.setAuthenticatorId(result.getKeyId().getId().getBytes());
        credential.setPublicKey(result.getPublicKeyCose().getBytes());
        credential.setSignatureCount(result.getSignatureCount());
        credential.setUser(user);
        result.isDiscoverable()
                .ifPresent(credential::setIsDiscoverable);

        return credential;
    }

}
