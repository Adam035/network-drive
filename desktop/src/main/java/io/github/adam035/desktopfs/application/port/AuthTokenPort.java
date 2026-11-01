package io.github.adam035.desktopfs.application.port;

public interface AuthTokenPort {

    void rotateAuthTokens(String refreshToken);

}
