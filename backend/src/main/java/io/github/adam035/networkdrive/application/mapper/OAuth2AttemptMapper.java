package io.github.adam035.networkdrive.application.mapper;

import io.github.adam035.networkdrive.application.dto.OAuth2Attempt;
import io.github.adam035.networkdrive.application.dto.OAuth2Command;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OAuth2AttemptMapper {

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "clientId", source = "oAuth2Command.clientId")
    @Mapping(target = "redirectUri", source = "oAuth2Command.redirectUri")
    @Mapping(target = "responseType", source = "oAuth2Command.responseType")
    @Mapping(target = "codeChallenge", source = "oAuth2Command.codeChallenge")
    @Mapping(target = "codeChallengeMethod", source = "oAuth2Command.codeChallengeMethod")
    OAuth2Attempt mapToOAuth2Attempt(OAuth2Command oAuth2Command, String userId);

}
