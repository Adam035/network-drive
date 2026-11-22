package io.github.adam035.desktopfs.application.usecase;

import io.github.adam035.desktopfs.application.auth.AuthSession;
import io.github.adam035.desktopfs.application.port.*;
import io.github.adam035.desktopfs.domain.service.VolumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StartApplicationUseCase {

    private final ConfigurableApplicationContext context;

    private final ApplicationArguments applicationArguments;

    private final AuthSession authSession;

    private final SecureStorePort secureStorePort;

    private final AuthTokenPort authTokenPort;

    private final OAuth2Port oAuth2Port;

    private final MountPort mountPort;

    private final VolumePort volumePort;

    private final VolumeService volumeService;

    private final DeviceRegistrationPort deviceRegistrationPort;

    public void startApplication() {
        Optional<URI> callback = applicationArguments.getNonOptionArgs().stream()
                .filter(arg -> arg.startsWith("network-drive:"))
                .map(URI::create)
                .findFirst();

        if (callback.isPresent()) {
            String code = getParameter(callback.get(), "code");
            String state = getParameter(callback.get(), "state");

            authorize(code, state);
        }

        authSession.getRefreshToken()
                .ifPresentOrElse(
                        authTokenPort::rotateAuthTokens,
                        () -> {
                            oAuth2Port.authorize();
                            exit();
                        }
                );

        if (secureStorePort.get("device-id").isEmpty()) {
            deviceRegistrationPort.registerDevice();
        }

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

    private String getParameter(URI uri, String parameter) {
        return UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .getFirst(parameter);
    }

    private void authorize(String code, String state) {
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

        boolean stateMatches = secureStorePort.get("state")
                .map(storedState -> encoder.encodeToString(storedState).equals(state))
                .orElse(false);

        if (!stateMatches) {
            throw new IllegalStateException("State does not match"); // TODO
        }

        secureStorePort.get("code-verifier").ifPresentOrElse(
                storedCodeVerifier ->
                        authTokenPort.exchangeCode(code, new String(storedCodeVerifier, StandardCharsets.UTF_8)),
                () -> {
                     throw new IllegalStateException("Code verifier not found"); // TODO
                }
        );
    }

}
