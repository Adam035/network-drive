package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.dto.OAuth2Result;
import io.github.adam035.networkdrive.application.usecase.AuthorizeDesktopUseCase;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.OAuth2Request;
import io.github.adam035.networkdrive.infrastructure.spring.web.mapper.OAuth2Mapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth/oauth2")
public class OAuth2Controller {

    private final OAuth2Mapper oAuth2Mapper;

    private final AuthorizeDesktopUseCase authorizeDesktopUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public OAuth2Result authorize(@ModelAttribute OAuth2Request oAuth2Request) {
        return authorizeDesktopUseCase.authorizeDesktop(oAuth2Mapper.mapToOAuh2Command(oAuth2Request));
    }

}
