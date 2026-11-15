package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.AuthTokensResult;
import io.github.adam035.networkdrive.application.dto.ExchangeCodeCommand;
import io.github.adam035.networkdrive.application.dto.OAuth2Attempt;
import io.github.adam035.networkdrive.application.exception.AuthorizationException;
import io.github.adam035.networkdrive.application.port.AuthTokenPort;
import io.github.adam035.networkdrive.application.port.AuthorizationCodeStorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class ExchangeCodeUseCode {

    private final AuthorizationCodeStorePort authorizationCodeStorePort;

    private final MessageDigest messageDigest;

    private final AuthTokenPort authTokenPort;

    public AuthTokensResult exchangeCode(ExchangeCodeCommand exchangeCodeCommand) {
        OAuth2Attempt oAuth2Attempt = authorizationCodeStorePort.get(exchangeCodeCommand.code())
                .orElseThrow(() -> new AuthorizationException(""));

        boolean isValid = verifyCode(oAuth2Attempt.codeChallenge(), exchangeCodeCommand.codeVerifier());

        if (!isValid) {
            throw new AuthorizationException("");
        }

        return authTokenPort.generateAuthTokens(oAuth2Attempt.userId());
    }

    private boolean verifyCode(String codeChallenge, String codeVerifier) {
        byte[] digest = messageDigest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(digest)
                .equals(codeChallenge);
    }

}
