package io.github.adam035.networkdrive.infrastructure.persistence.repository;

import io.github.adam035.networkdrive.domain.model.Device;
import io.github.adam035.networkdrive.domain.repository.DeviceRepository;
import io.github.adam035.networkdrive.infrastructure.persistence.mapper.DeviceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceDbRepository implements DeviceRepository {

    private final DeviceJpaRepository deviceJpaRepository;

    private final DeviceMapper deviceMapper;

    @Override
    public Device save(Device device) {
        return deviceMapper.mapToModel(deviceJpaRepository.save(deviceMapper.mapToEntity(device)));
    }

    @Override
    public Optional<Device> findById(String id) {
        return deviceJpaRepository.findById(id)
                .map(deviceMapper::mapToModel);
    }

}
