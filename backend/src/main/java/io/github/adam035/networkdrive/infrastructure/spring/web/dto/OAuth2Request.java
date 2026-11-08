package io.github.adam035.networkdrive.infrastructure.spring.web.dto;

import org.springframework.web.bind.annotation.BindParam;

public record OAuth2Request(
        @BindParam("client_id") String clientId,
        @BindParam("response_type") String responseType,
        @BindParam("code_challenge") String codeChallenge,
        @BindParam("code_challenge_method") String codeChallengeMethod
) {
}
