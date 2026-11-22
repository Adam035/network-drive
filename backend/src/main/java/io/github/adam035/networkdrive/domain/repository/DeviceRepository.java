package io.github.adam035.networkdrive.domain.repository;

import io.github.adam035.networkdrive.domain.model.Device;

import java.util.Optional;

public interface DeviceRepository {

    Device save(Device device);

    Optional<Device> findById(String id);

}
