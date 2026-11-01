package io.github.adam035.desktopfs.application.auth;

import io.github.adam035.desktopfs.application.port.SecureStorePort;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthSession {

    private final SecureStorePort secureStorePort;
    @Setter
    private volatile String accessToken;

    public Optional<String> getAccessToken() {
        return Optional.ofNullable(accessToken);
    }

    public Optional<String> getRefreshToken() {
        return secureStorePort.get("refresh-token").map(bytes -> new String(bytes, StandardCharsets.UTF_8));
    }

    public void setRefreshToken(String refreshToken) {
        secureStorePort.put("refresh-token", refreshToken.getBytes(StandardCharsets.UTF_8));
    }

}
