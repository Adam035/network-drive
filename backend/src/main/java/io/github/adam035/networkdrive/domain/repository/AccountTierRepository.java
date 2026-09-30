package io.github.adam035.networkdrive.domain.repository;

import io.github.adam035.networkdrive.domain.model.AccountTier;

import java.util.List;

public interface AccountTierRepository {

    List<AccountTier> findAll();

}
