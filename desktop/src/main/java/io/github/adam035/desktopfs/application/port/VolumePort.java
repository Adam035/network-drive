package io.github.adam035.desktopfs.application.port;

import io.github.adam035.desktopfs.application.dto.VolumeResult;

import java.util.Optional;
import java.util.Set;

public interface VolumePort {

    Optional<VolumeResult> getVolume(String volumeLabel);

    Set<String> getAvailableVolumeLabels();

}
