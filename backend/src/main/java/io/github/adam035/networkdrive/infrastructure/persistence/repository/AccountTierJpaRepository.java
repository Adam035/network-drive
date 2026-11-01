package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.infrastructure.persistence.entity.AccountTierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTierJpaRepository extends JpaRepository<AccountTierEntity, String> {

    Optional<AccountTierEntity> findByName(String name);

}
