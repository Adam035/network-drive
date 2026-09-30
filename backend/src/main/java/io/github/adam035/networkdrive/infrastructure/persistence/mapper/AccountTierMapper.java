package io.github.adam035.networkdrive.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.domain.model.AccountTier;
import io.github.adam035.networkdrive.infrastructure.persistence.entity.AccountTierEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountTierMapper {

    AccountTier mapToModel(AccountTierEntity source);

    AccountTierEntity mapToEntity(AccountTier source);

}
