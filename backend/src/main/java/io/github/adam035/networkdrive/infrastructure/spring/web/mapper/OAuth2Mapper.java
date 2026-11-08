package io.github.adam035.networkdrive.infrastructure.spring.web.mapper;

import io.github.adam035.networkdrive.application.dto.OAuth2Command;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.OAuth2Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Locale;

@Mapper(componentModel = "spring")
public interface OAuth2Mapper {

    @Mapping(source = "responseType", target = "responseType", qualifiedByName = "mapToResponseType")
    @Mapping(source = "codeChallengeMethod", target = "codeChallengeMethod", qualifiedByName = "mapToCodeChallengeMethod")
    OAuth2Command mapToOAuh2Command(OAuth2Request oAuth2Request);

    @Named("mapToResponseType")
    default OAuth2Command.ResponseType mapToResponseType(String responseType) {
        return switch (responseType.toLowerCase(Locale.ROOT)) {
            case "code" -> OAuth2Command.ResponseType.CODE;
            case "token" -> OAuth2Command.ResponseType.TOKEN;
            default -> throw new IllegalArgumentException(
                    "Unsupported response type: ".concat(responseType)
            );
        };
    }

    @Named("mapToCodeChallengeMethod")
    default OAuth2Command.CodeChallengeMethod mapToCodeChallengeMethod(String codeChallengeMethod) {
        return switch (codeChallengeMethod.toLowerCase(Locale.ROOT)) {
            case "plain" -> OAuth2Command.CodeChallengeMethod.PLAIN;
            case "s256" -> OAuth2Command.CodeChallengeMethod.S256;
            default -> throw new IllegalArgumentException(
                    "Unsupported code challenge method: ".concat(codeChallengeMethod)
            );
        };
    }

}
