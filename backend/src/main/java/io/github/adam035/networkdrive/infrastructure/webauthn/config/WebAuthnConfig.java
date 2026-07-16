package io.github.adam035.networkdrive.infrastructure.webauthn.config;

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
                        .id("network-drive.local")
                        .name("Network Drive")
                        .build()
                )
                .credentialRepository(credentialRepository)
                .origins(Set.of("https://network-drive.local"))
                .build();
    }

}
