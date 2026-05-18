package io.github.adam035.networkdrive.auth.infrastructure.inbound.jwt;

import io.github.adam035.networkdrive.auth.application.port.in.JwtPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtAdapter implements JwtPort {

    private final SecretKey secretKey;

    public String generateToken(String subject, Map<String, Object> claims, TemporalAmount ttl) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public boolean validateToken(String token, String subject) {
        String extractedSubject = extractClaim(token, Claims::getSubject);
        return extractedSubject.equals(subject) && validateToken(token);
    }

    public boolean validateTokenType(String token, String tokenType) {
        String extractedTokenType = extractClaim(token, claims -> claims.get("token_type", String.class));
        return extractedTokenType.equals(tokenType) && validateToken(token);
    }

    public boolean validateToken(String token, String subject, String tokenType) {
        String extractedTokenType = extractClaim(token, claims -> claims.get("token_type", String.class));
        return validateToken(token, subject) && extractedTokenType.equals(tokenType);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .before(Date.from(Instant.now()));
    }

}
