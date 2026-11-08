package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.OAuth2Command;
import io.github.adam035.networkdrive.application.dto.OAuth2Result;
import io.github.adam035.networkdrive.application.exception.AuthorizationException;
import org.springframework.stereotype.Component;

import static io.github.adam035.networkdrive.application.dto.OAuth2Command.ResponseType.CODE;
import static io.github.adam035.networkdrive.application.dto.OAuth2Command.CodeChallengeMethod.S256;

@Component
public class AuthorizeDesktopUseCase {

    public OAuth2Result authorizeDesktop(OAuth2Command oAuth2Command) {
        checkParameters(oAuth2Command);
        return null;
    }

    private void checkParameters(OAuth2Command oAuth2Command) {
        if (!oAuth2Command.clientId().equals("desktop")) {
            throw new AuthorizationException("Unsupported client ID: ".concat(oAuth2Command.clientId()));
        }

        if (!oAuth2Command.responseType().equals(CODE)) {
            throw new AuthorizationException("Unsupported response type: ".concat(oAuth2Command.clientId()));
        }

        if (!oAuth2Command.codeChallengeMethod().equals(S256)) {
            throw new AuthorizationException("Unsupported code challenge method: ".concat(oAuth2Command.clientId()));
        }
    }

}
