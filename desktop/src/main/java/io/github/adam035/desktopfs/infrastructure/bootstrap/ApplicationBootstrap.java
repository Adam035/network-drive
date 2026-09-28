package io.github.adam035.desktopfs.infrastructure.bootstrap;

import io.github.adam035.desktopfs.application.usecase.MountVolumesUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationBootstrap {

    private final MountVolumesUseCase mountVolumesUseCase;

    @PostConstruct
    public void start() {
        mountVolumesUseCase.mountVolumes();
    }

}
