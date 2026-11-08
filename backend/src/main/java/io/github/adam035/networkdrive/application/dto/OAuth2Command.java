package io.github.adam035.networkdrive.application.dto;

import io.github.adam035.networkdrive.application.enumerate.CodeChallengeMethod;
import io.github.adam035.networkdrive.application.enumerate.ResponseType;

public record OAuth2Command(
        String clientId,
        String redirectUri,
        ResponseType responseType,
        String codeChallenge,
        CodeChallengeMethod codeChallengeMethod
) {
}
