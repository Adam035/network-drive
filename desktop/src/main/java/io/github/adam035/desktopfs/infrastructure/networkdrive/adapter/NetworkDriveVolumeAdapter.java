package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.auth.AuthSession;
import io.github.adam035.desktopfs.application.dto.VolumeResult;
import io.github.adam035.desktopfs.application.port.VolumePort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NetworkDriveVolumeAdapter implements VolumePort {

    private final AuthSession authSession;

    private final RestClient networkDriveClient;

    @Override
    public Optional<VolumeResult> getVolume(String volumeLabel) {
        String authToken = authSession.getAccessToken()
                .orElseThrow(() -> new IllegalStateException("Access token is not available"));

        try {
            VolumeResult volume = networkDriveClient.get()
                    .uri("/api/volumes/".concat(volumeLabel))
                    .header("Authorization", "Bearer ".concat(authToken))
                    .retrieve()
                    .body(VolumeResult.class);

            return Optional.ofNullable(volume);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public Set<String> getAvailableVolumeLabels() {
        String authToken = authSession.getAccessToken()
                .orElseThrow(() -> new IllegalStateException("Access token is not available"));

        try {
            return networkDriveClient.get()
                    .uri("/api/volumes")
                    .header("Authorization", "Bearer ".concat(authToken))
                    .retrieve()
                    .body(ParameterizedTypeReference.forType(Set.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Set.of();
        }
    }

}
