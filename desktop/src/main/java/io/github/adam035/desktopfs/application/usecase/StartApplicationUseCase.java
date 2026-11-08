package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.auth.AuthSession;
import io.github.adam035.desktopfs.application.port.*;
import io.github.adam035.desktopfs.domain.service.VolumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StartApplicationUseCase {

    private final ConfigurableApplicationContext context;

    private final ApplicationArguments applicationArguments;

    private final AuthSession authSession;

    private final AuthTokenPort authTokenPort;

    private final OAuth2Port oAuth2Port;

    private final MountPort mountPort;

    private final VolumePort volumePort;

    private final VolumeService volumeService;  

    public void startApplication() {
        if (isCallback()) {
            String code = Objects.requireNonNull(applicationArguments.getOptionValues("code")).getFirst();
            String state = Objects.requireNonNull(applicationArguments.getOptionValues("state")).getFirst();

            authorize(code, state);
            return;
        }

        authSession.getRefreshToken()
                .ifPresentOrElse(
                        authTokenPort::rotateAuthTokens,
                        () -> {
                            oAuth2Port.authorize();
                            exit();
                        }
                );

        mountVolumes();
    }

    private void mountVolumes() {
        volumePort.getAvailableVolumeLabels()
                .forEach(volumeLabel -> mountPort.mountVolume(volumeLabel, volumeService.chooseVolumePath()));
    }

    private void exit() {
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }

    private boolean isCallback() {
        return applicationArguments.containsOption("callback")
                && applicationArguments.containsOption("code")
                && applicationArguments.containsOption("state");
    }

    private void authorize(String code, String state) {
        Boolean stateMatches = authSession.getState()
                .map(storedState -> storedState.equals(state))
                .orElse(false);

        if (!stateMatches) {
            throw new IllegalStateException("State does not match"); // TODO
        }

        authTokenPort.exchangeCode(code);
    }

}
