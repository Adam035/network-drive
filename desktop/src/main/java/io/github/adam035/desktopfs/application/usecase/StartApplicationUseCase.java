package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.auth.AuthSession;
import io.github.adam035.desktopfs.application.port.AuthTokenPort;
import io.github.adam035.desktopfs.application.port.LoginPort;
import io.github.adam035.desktopfs.application.port.MountPort;
import io.github.adam035.desktopfs.application.port.VolumePort;
import io.github.adam035.desktopfs.domain.service.VolumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartApplicationUseCase {

    private final AuthSession authSession;

    private final AuthTokenPort authTokenPort;

    private final LoginPort loginPort;

    private final MountPort mountPort;

    private final VolumePort volumePort;

    private final VolumeService volumeService;

    public void startApplication() {
        authSession.getRefreshToken()
                .ifPresentOrElse(authTokenPort::rotateAuthTokens, loginPort::login);

        mountVolumes();
    }

    private void mountVolumes() {
        volumePort.getAvailableVolumeLabels()
                .forEach(volumeLabel -> mountPort.mountVolume(volumeLabel, volumeService.chooseVolumePath()));
    }

}
