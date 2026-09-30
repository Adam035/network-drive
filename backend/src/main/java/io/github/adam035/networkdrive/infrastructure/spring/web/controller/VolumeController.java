package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.dto.VolumeResult;
import io.github.adam035.networkdrive.application.usecase.GetAvailableVolumeLabelsUseCase;
import io.github.adam035.networkdrive.application.usecase.GetVolumeUseCase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/volumes")
public class VolumeController {

    private final GetVolumeUseCase getVolumeUseCase;

    private final GetAvailableVolumeLabelsUseCase getAvailableVolumeLabelsUseCase;

    @GetMapping("/{volumeLabel}")
    public VolumeResult getVolume(@PathVariable String volumeLabel) {
        return getVolumeUseCase.getVolume(volumeLabel);
    }

    @GetMapping
    public Set<String> getAvailableVolumeLabels() {
        return getAvailableVolumeLabelsUseCase.getAvailableVolumeLabels();
    }

}
