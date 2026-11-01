package io.github.adam035.desktopfs.domain.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class VolumeService {

    public Path chooseVolumePath() {
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
