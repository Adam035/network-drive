package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.RegisterDeviceCommand;
import io.github.adam035.networkdrive.application.exception.DeviceAlreadyRegisteredException;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.Device;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RegisterDeviceUseCase {

    private final AuthUserExtractorPort authUserExtractorPort;

    private final DeviceRepository deviceRepository;

    public void registerDevice(RegisterDeviceCommand registerDeviceCommand) {
        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        deviceRepository.findById(registerDeviceCommand.deviceId())
                .orElseThrow(DeviceAlreadyRegisteredException::new);

        Device device = createDevice(registerDeviceCommand.deviceId(), user, registerDeviceCommand.publicKey());

        deviceRepository.save(device);
    }

    private Device createDevice(String deviceId, User user, byte[] publicKey) {
        Device device = new Device();

        device.setId(deviceId);
        device.setUser(user);
        device.setPublicKey(publicKey);
        device.setCreatedAt(Instant.now());
        device.setUpdatedAt(Instant.now());

        return device;
    }

}
