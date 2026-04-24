package io.github.adam035.networkdrive.auth.infrastructure.outbound.persistence.repository;

import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.*;
import io.github.adam035.networkdrive.auth.application.port.out.UserRepositoryPort;
import io.github.adam035.networkdrive.auth.domain.model.Credential;
import io.github.adam035.networkdrive.common.domain.model.User;
import io.github.adam035.networkdrive.auth.domain.repository.CredentialRepository;
import io.github.adam035.networkdrive.auth.infrastructure.outbound.persistence.entity.CredentialEntity;
import io.github.adam035.networkdrive.auth.infrastructure.outbound.persistence.mapper.CredentialMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class CredentialDbRepository implements CredentialRepository, com.yubico.webauthn.CredentialRepository {

    private final CredentialJpaRepository credentialJpaRepository;

    private final CredentialMapper credentialMapper;

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Credential save(Credential credential) {
        CredentialEntity credentialEntity = credentialMapper.mapToEntity(credential);
        return credentialMapper.mapToModel(credentialJpaRepository.save(credentialEntity));
    }

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        return userRepositoryPort.findByUsername(username)
                .map(user -> credentialJpaRepository.findByUserId(user.getId()).stream()
                        .map(credential -> PublicKeyCredentialDescriptor.builder()
                                .id(new ByteArray(credential.getAuthenticatorId()))
                                .type(PublicKeyCredentialType.PUBLIC_KEY)
                                .build()
                        )
                        .collect(Collectors.toSet())
                )
                .orElse(Set.of());
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return userRepositoryPort.findByUsername(username)
                .map(user -> new ByteArray(user.getId().getBytes()));
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userId) {
        return userRepositoryPort.findById(new String(userId.getBytes(), StandardCharsets.UTF_8))
                .map(User::getUsername);
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray authenticatorId, ByteArray userId) {
        String decodedUserId = new String(userId.getBytes(), StandardCharsets.UTF_8);
        return credentialJpaRepository
                .findByAuthenticatorIdAndUserId(authenticatorId.getBytes(), decodedUserId)
                .map(credentialMapper::mapToRegisteredCredential);
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray authenticatorId) {
        return credentialJpaRepository.findByAuthenticatorId(authenticatorId.getBytes()).stream()
                .map(credentialMapper::mapToRegisteredCredential)
                .collect(Collectors.toSet());
    }

}
