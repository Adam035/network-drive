package io.github.adam035.desktopfs.application.dto;

public record VolumeResult(
        String volumeLabel,
        long totalSize,
        long freeSize
) {
}
