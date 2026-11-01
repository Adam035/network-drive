package io.github.adam035.networkdrive.domain.repository;

import io.github.adam035.networkdrive.domain.model.AccountTier;

import java.util.Optional;

public interface AccountTierRepository {

    Optional<AccountTier> findByName(String name);

}
