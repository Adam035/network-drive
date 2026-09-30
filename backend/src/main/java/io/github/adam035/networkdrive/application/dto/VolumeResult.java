package io.github.adam035.networkdrive.application.dto;

public record VolumeResult(
        String volumeLabel,
        long totalSize,
        long freeSize
) {
}
