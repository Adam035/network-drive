package io.github.adam035.networkdrive.auth.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.auth.infrastructure.persistence.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CredentialJpaRepository extends JpaRepository<CredentialEntity, String> {

    List<CredentialEntity> findByAuthenticatorId(byte[] credentialId);

    List<CredentialEntity> findByUser_Username(String username);

    Optional<CredentialEntity> findByAuthenticatorIdAndUserId(byte[] authenticatorId, String userId);

}
