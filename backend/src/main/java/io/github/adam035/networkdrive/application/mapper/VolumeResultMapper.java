package io.github.adam035.networkdrive.application.mapper;

import io.github.adam035.networkdrive.application.dto.VolumeResult;
import io.github.adam035.networkdrive.domain.model.AccountTier;
import io.github.adam035.networkdrive.domain.model.Directory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VolumeResultMapper {

    @Mapping(target = "volumeLabel", source = "directory.name")
    @Mapping(target = "totalSize", source = "accountTier.maxVolumeSize")
    @Mapping(target = "freeSize", expression = "java(accountTier.getMaxVolumeSize() - directory.getSize())")
    VolumeResult mapToVolumeResult(Directory directory, AccountTier accountTier);

}
