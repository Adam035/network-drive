package io.github.adam035.desktopfs.infrastructure.bootstrap;

import io.github.adam035.desktopfs.application.usecase.MountVolumesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationBootstrap {

    private final MountVolumesUseCase mountVolumesUseCase;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        mountVolumesUseCase.mountVolumes();
    }

}
