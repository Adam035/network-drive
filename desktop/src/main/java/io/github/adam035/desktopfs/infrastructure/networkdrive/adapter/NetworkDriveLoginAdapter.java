package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.port.LoginPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.awt.*;

@Component
@RequiredArgsConstructor
public class NetworkDriveLoginAdapter implements LoginPort {

    private final String loginPageUrl;

    @Override
    public void login() {
        if (!isDesktopSupported()) {
            throw new UnsupportedOperationException("Desktop is not supported on this platform");
        }

        String state = java.util.UUID.randomUUID().toString();

        try {
            Desktop.getDesktop().browse(new DefaultUriBuilderFactory().builder()
                    .path(loginPageUrl)
                    .queryParam("client", "desktop")
                    .queryParam("state", state)
                    .build()
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to open login page in browser", e);
        }
    }

    private boolean isDesktopSupported() {
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
    }

}
