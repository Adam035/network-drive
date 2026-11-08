package io.github.adam035.networkdrive.application.dto;

public record OAuth2Command(
        String clientId,
        ResponseType responseType,
        String codeChallenge,
        CodeChallengeMethod codeChallengeMethod
) {

    public enum CodeChallengeMethod {
        PLAIN,
        S256
    }

    public enum ResponseType {
        CODE,
        TOKEN
    }

}
