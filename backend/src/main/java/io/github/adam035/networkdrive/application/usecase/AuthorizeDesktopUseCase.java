package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.dto.OAuth2Command;
import io.github.adam035.networkdrive.application.dto.OAuth2Result;
import io.github.adam035.networkdrive.application.exception.AuthorizationException;
import io.github.adam035.networkdrive.application.mapper.OAuth2AttemptMapper;
import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.adam035.networkdrive.application.enumerate.ResponseType.CODE;
import static io.github.adam035.networkdrive.application.enumerate.CodeChallengeMethod.S256;

@Component
@RequiredArgsConstructor
public class AuthorizeDesktopUseCase {

    private final AuthUserExtractorPort authUserExtractorPort;

    private final OAuth2AttemptMapper oAuth2AttemptMapper;

    public OAuth2Result authorizeDesktop(OAuth2Command oAuth2Command) {
        checkParameters(oAuth2Command);

        authUserExtractorPort.extractUser()
                .map(user -> oAuth2AttemptMapper.mapToOAuth2Attempt(oAuth2Command, user.getId()))
                .orElseThrow(UserDoesNotExist::new);

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
