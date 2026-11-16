package io.github.adam035.desktopfs.infrastructure.networkdrive.adapter;

import io.github.adam035.desktopfs.application.auth.AuthSession;
import io.github.adam035.desktopfs.application.port.AuthTokenPort;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.ExchangeCodeRequest;
import io.github.adam035.desktopfs.infrastructure.networkdrive.dto.AuthTokensResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NetworkDriveAuthTokenAdapter implements AuthTokenPort {

    private final AuthSession authSession;

    private final RestClient networkDriveClient;

    @Override
    public void rotateAuthTokens(String refreshToken) {
        AuthTokensResponse authTokensResponse = networkDriveClient.post()
                .uri("/api/auth/token/rotate")
                .header(HttpHeaders.COOKIE, "refreshToken=".concat(refreshToken))
                .retrieve()
                .body(AuthTokensResponse.class);

        if (authTokensResponse == null) {
            throw new IllegalStateException("Failed to rotate auth tokens");
        }

        authSession.setAccessToken(authTokensResponse.accessToken());
        authSession.setRefreshToken(authTokensResponse.refreshToken());
    }

    @Override
    public void exchangeCode(String code, String codeVerifier) {
        AuthTokensResponse authTokensResponse = networkDriveClient.post()
                .uri("/api/auth/token/exchange-code")
                .body(new ExchangeCodeRequest(code, codeVerifier))
                .retrieve()
                .body(AuthTokensResponse.class);

        if (authTokensResponse == null) {
            throw new IllegalStateException("Failed to rotate auth tokens");
        }

        authSession.setAccessToken(authTokensResponse.accessToken());
        authSession.setRefreshToken(authTokensResponse.refreshToken());
    }

}
