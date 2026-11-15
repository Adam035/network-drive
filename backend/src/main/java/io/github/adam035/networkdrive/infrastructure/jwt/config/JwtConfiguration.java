package io.github.adam035.networkdrive.infrastructure.jwt.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfiguration {

    @Bean
    public SecretKey jwtSecret(@Value("${jwt.secret}") String jwtSecretBase64) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretBase64));
    }

}
