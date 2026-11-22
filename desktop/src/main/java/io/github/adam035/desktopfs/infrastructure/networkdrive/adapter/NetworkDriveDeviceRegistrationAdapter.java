package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.auth.AuthSession;
import io.github.adam035.desktopfs.application.dto.KeyPair;
import io.github.adam035.desktopfs.application.port.DeviceRegistrationPort;
import io.github.adam035.desktopfs.application.port.KeyGeneratorPort;
import io.github.adam035.desktopfs.application.port.SecureStorePort;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.RegisterDeviceRequest;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.RegisterDeviceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class NetworkDriveDeviceRegistrationAdapter implements DeviceRegistrationPort {

    private final AuthSession authSession;

    private final RestClient networkDriveClient;

    private final SecureStorePort secureStorePort;

    private final KeyGeneratorPort keyGeneratorPort;

    @Override
    public void registerDevice() {
        String authToken = authSession.getAccessToken()
                .orElseThrow(() -> new IllegalStateException("Access token is not available"));

        KeyPair keyPair = keyGeneratorPort.generateKeyPair();
        secureStorePort.put("device-private-key", keyPair.privateKey());

        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        RegisterDeviceResponse registerDeviceResponse = networkDriveClient.post()
                .uri("/api/device/register")
                .header("Authorization", "Bearer ".concat(authToken))
                .body(new RegisterDeviceRequest(encoder.encodeToString(keyPair.publicKey())))
                .retrieve()
                .body(RegisterDeviceResponse.class);

        if (registerDeviceResponse == null) {
            throw new IllegalStateException("Device registration returned no device ID");
        }

        secureStorePort.put("device-id", registerDeviceResponse.deviceId().getBytes(StandardCharsets.UTF_8));
    }

}
