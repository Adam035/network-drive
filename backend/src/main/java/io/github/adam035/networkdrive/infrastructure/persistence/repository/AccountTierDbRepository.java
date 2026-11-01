package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.domain.model.AccountTier;
import io.github.adam035.networkdrive.domain.repository.AccountTierRepository;
import io.github.adam035.networkdrive.infrastructure.persistence.mapper.AccountTierMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AccountTierDbRepository implements AccountTierRepository {

    private final AccountTierJpaRepository accountTierJpaRepository;

    private final AccountTierMapper accountTierMapper;

    @Override
    public Optional<AccountTier> findByName(String name) {
        return accountTierJpaRepository.findByName(name)
                .map(accountTierMapper::mapToModel);
    }

}
