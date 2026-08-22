package io.github.adam035.desktopfs.application.port;

import io.github.adam035.desktopfs.application.dto.VolumeResult;

import java.util.List;
import java.util.Optional;

public interface VolumePort {

    Optional<VolumeResult> getVolume(String volumeLabel);

    List<String> getAvailableVolumeLabels();

}
