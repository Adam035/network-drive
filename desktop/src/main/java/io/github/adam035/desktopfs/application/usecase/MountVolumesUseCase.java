package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.port.MountPort;
import io.github.adam035.desktopfs.application.port.VolumePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MountVolumesUseCase {

    private final VolumePort volumePort;

    private final MountPort mountPort;

    public void mountVolumes() {
        volumePort.getAvailableVolumeLabels()
                .forEach(volumeLabel -> mountPort.mountVolume(volumeLabel, chooseVolumePath()));
    }

    private Path chooseVolumePath() {
        List<Character> drivePaths = Arrays.stream(File.listRoots())
                .map(file -> file.getPath().charAt(0))
                .toList();

        for (char letter = 'Z'; letter >= 'A'; letter--) {
            if (!drivePaths.contains(letter)) {
                return Path.of(String.format("%s:", letter));
            }
        }

        throw new RuntimeException("No available drive paths found"); // TODO
    }

}
