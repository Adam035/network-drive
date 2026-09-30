package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.domain.model.AccountTier;
import io.github.adam035.networkdrive.domain.repository.AccountTierRepository;
import io.github.adam035.networkdrive.infrastructure.persistence.mapper.AccountTierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountTierDbRepository implements AccountTierRepository {

    private final AccountTierJpaRepository accountTierJpaRepository;

    private final AccountTierMapper accountTierMapper;

    public final List<AccountTier> findAll() {
        return accountTierJpaRepository.findAll().stream()
                .map(accountTierMapper::mapToModel)
                .toList();
    }

}
