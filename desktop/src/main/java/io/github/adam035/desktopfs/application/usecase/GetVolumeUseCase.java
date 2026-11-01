package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.dto.VolumeResult;
import io.github.adam035.desktopfs.application.port.VolumePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetVolumeUseCase {

    private final VolumePort volumePort;

    public Optional<VolumeResult> getVolume(String volumeLabel) {
        return volumePort.getVolume(volumeLabel);
    }

}
