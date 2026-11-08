package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.port.OAuth2Port;
import io.github.adam035.desktopfs.application.port.SecureStorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.awt.*;
import java.net.URI;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class NetworkDriveOAuth2Adapter implements OAuth2Port {

    private final SecureRandom secureRandom;

    private final MessageDigest messageDigest;

    private final SecureStorePort secureStorePort;

    private final String loginPageUrl;

    @Override
    public void authorize() {
        if (!isDesktopSupported()) {
            throw new UnsupportedOperationException("Desktop is not supported on this platform");
        }

        byte[] codeChallenge = messageDigest.digest(generateCodeVerifier());
        byte[] state = generateState();

        try {
            Desktop.getDesktop().browse(buildUri(codeChallenge, state));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to open login page in browser", e);
        }
    }

    private boolean isDesktopSupported() {
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
    }

    private byte[] generateCodeVerifier() {
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        secureStorePort.put("code-verifier", codeVerifier);

        return codeVerifier;
    }

    private byte[] generateState() {
        byte[] state = new byte[32];
        secureRandom.nextBytes(state);
        secureStorePort.put("state", state);

        return state;
    }

    private URI buildUri(byte[] codeChallenge, byte[] state) {
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

        return new DefaultUriBuilderFactory().builder()
                .path(loginPageUrl)
                .queryParam("client_id", "desktop")
                .queryParam("redirect_uri", "network-drive://callback")
                .queryParam("response_type", "code")
                .queryParam("code_challenge", encoder.encodeToString(codeChallenge))
                .queryParam("code_challenge_method", "S256")
                .queryParam("state", encoder.encodeToString(state))
                .build();
    }

}
