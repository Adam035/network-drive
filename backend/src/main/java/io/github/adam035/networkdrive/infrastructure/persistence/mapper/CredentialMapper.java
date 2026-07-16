package io.github.adam035.networkdrive.infrastructure.persistence.mapper;

import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import io.github.adam035.networkdrive.domain.model.Credential;
import io.github.adam035.networkdrive.infrastructure.persistence.entity.CredentialEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialMapper {

    Credential mapToModel(CredentialEntity source);

    CredentialEntity mapToEntity(Credential source);

    default RegisteredCredential mapToRegisteredCredential(CredentialEntity source) {
        return RegisteredCredential.builder()
                .credentialId(new ByteArray(source.getAuthenticatorId()))
                .userHandle(new ByteArray(source.getUserId().getBytes()))
                .publicKeyCose(new ByteArray(source.getPublicKey()))
                .signatureCount(source.getSignatureCount())
                .build();
    }

}
