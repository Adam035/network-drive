package io.github.adam035.networkdrive.infrastructure.persistence.mapper;

import io.github.adam035.networkdrive.domain.model.Device;
import io.github.adam035.networkdrive.infrastructure.persistence.entity.DeviceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    Device mapToModel(DeviceEntity source);

    DeviceEntity mapToEntity(Device source);

}
