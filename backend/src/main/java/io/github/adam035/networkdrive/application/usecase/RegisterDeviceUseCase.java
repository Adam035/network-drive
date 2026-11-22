package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.RegisterDeviceCommand;
import io.github.adam035.networkdrive.application.dto.RegisterDeviceResult;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.Device;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegisterDeviceUseCase {

    private final AuthUserExtractorPort authUserExtractorPort;

    private final DeviceRepository deviceRepository;

    public RegisterDeviceResult registerDevice(RegisterDeviceCommand registerDeviceCommand) {
        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        Device device = createDevice(user, registerDeviceCommand.publicKey());
        String deviceId = deviceRepository.save(device).getId();

        return new RegisterDeviceResult(deviceId);
    }

    private Device createDevice(User user, byte[] publicKey) {
        Device device = new Device();

        device.setId(UUID.randomUUID().toString());
        device.setUser(user);
        device.setPublicKey(publicKey);
        device.setCreatedAt(Instant.now());
        device.setUpdatedAt(Instant.now());

        return device;
    }

}
