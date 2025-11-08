package io.github.adam035.networkdrive.user.config;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class WebAuthnConfig {

    @Bean
    public RelyingParty relyingParty(CredentialRepository credentialRepository) {
        return RelyingParty.builder()
                .identity(RelyingPartyIdentity.builder()
                        .id("localhost")
                        .name("Network Drive")
                        .build()
                )
                .credentialRepository(credentialRepository)
                .origins(Set.of(
                        "http://localhost:8080",
                        System.getenv("CHROME_EXTENSION")
                ))
                .build();
    }

}
