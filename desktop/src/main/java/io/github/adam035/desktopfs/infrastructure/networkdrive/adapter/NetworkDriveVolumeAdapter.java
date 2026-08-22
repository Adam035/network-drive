package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.dto.VolumeResult;
import io.github.adam035.desktopfs.application.port.VolumePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NetworkDriveVolumeAdapter implements VolumePort {

    private final RestClient networkDriveClient;

    @Override
    public Optional<VolumeResult> getVolume(String volumeLabel) {
        try {
            VolumeResult volume = networkDriveClient.get()
                    .uri("/api/volumes/".concat(volumeLabel))
                    .retrieve()
                    .body(VolumeResult.class);

            return Optional.ofNullable(volume);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public List<String> getAvailableVolumeLabels() {
        return List.of("user1");
    }

}
