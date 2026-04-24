package io.github.adam035.networkdrive.auth.infrastructure.outbound.persistence.repository;

import io.github.adam035.networkdrive.auth.infrastructure.outbound.persistence.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CredentialJpaRepository extends JpaRepository<CredentialEntity, String> {

    List<CredentialEntity> findByUserId(String userId);

    List<CredentialEntity> findByAuthenticatorId(byte[] credentialId);

    Optional<CredentialEntity> findByAuthenticatorIdAndUserId(byte[] authenticatorId, String userId);

}
