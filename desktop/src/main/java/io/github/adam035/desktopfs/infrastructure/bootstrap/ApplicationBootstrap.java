package io.github.adam035.desktopfs.infrastructure.bootstrap;

import io.github.adam035.desktopfs.application.usecase.StartApplicationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationBootstrap {

    private final StartApplicationUseCase startApplicationUseCase;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        startApplicationUseCase.startApplication();
    }

}
