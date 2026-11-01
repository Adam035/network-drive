package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.auth.AuthSession;
import io.github.adam035.desktopfs.application.port.AuthTokenPort;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.RotateAuthTokensRequest;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.RotateAuthTokensResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NetworkDriveAuthTokenAdapter implements AuthTokenPort {

    private final AuthSession authSession;

    private final RestClient networkDriveClient;

    @Override
    public void rotateAuthTokens(String refreshToken) {
        RotateAuthTokensResponse rotateAuthTokensResponse = networkDriveClient.post()
                .uri("/api/auth/token/rotate")
                .body(new RotateAuthTokensRequest(refreshToken))
                .retrieve()
                .body(RotateAuthTokensResponse.class);

        if (rotateAuthTokensResponse == null) {
            throw new IllegalStateException("Failed to rotate auth tokens");
        }

        authSession.setAccessToken(rotateAuthTokensResponse.accessToken());
        authSession.setRefreshToken(rotateAuthTokensResponse.refreshToken());
    }

}
