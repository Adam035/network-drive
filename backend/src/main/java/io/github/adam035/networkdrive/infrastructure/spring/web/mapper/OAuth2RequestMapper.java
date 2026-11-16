package io.github.adam035.networkdrive.infrastructure.spring.web.mapper;

import io.github.adam035.networkdrive.application.dto.OAuth2Command;
import io.github.adam035.networkdrive.application.enumerate.CodeChallengeMethod;
import io.github.adam035.networkdrive.application.enumerate.ResponseType;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.OAuth2Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Locale;

import static io.github.adam035.networkdrive.application.enumerate.ResponseType.CODE;
import static io.github.adam035.networkdrive.application.enumerate.ResponseType.TOKEN;
import static io.github.adam035.networkdrive.application.enumerate.CodeChallengeMethod.PLAIN;
import static io.github.adam035.networkdrive.application.enumerate.CodeChallengeMethod.S256;

@Mapper(componentModel = "spring")
public interface OAuth2RequestMapper {

    @Mapping(source = "responseType", target = "responseType", qualifiedByName = "mapToResponseType")
    @Mapping(source = "codeChallengeMethod", target = "codeChallengeMethod", qualifiedByName = "mapToCodeChallengeMethod")
    OAuth2Command mapToOAuh2Command(OAuth2Request oAuth2Request);

    @Named("mapToResponseType")
    default ResponseType mapToResponseType(String responseType) {
        return switch (responseType.toLowerCase(Locale.ROOT)) {
            case "code" -> CODE;
            case "token" -> TOKEN;
            default -> throw new IllegalArgumentException(
                    "Unsupported response type: ".concat(responseType)
            );
        };
    }

    @Named("mapToCodeChallengeMethod")
    default CodeChallengeMethod mapToCodeChallengeMethod(String codeChallengeMethod) {
        return switch (codeChallengeMethod.toLowerCase(Locale.ROOT)) {
            case "plain" -> PLAIN;
            case "s256" -> S256;
            default -> throw new IllegalArgumentException(
                    "Unsupported code challenge method: ".concat(codeChallengeMethod)
            );
        };
    }

}
