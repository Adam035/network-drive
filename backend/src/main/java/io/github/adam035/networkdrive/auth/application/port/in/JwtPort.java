package io.github.adam035.networkdrive.auth.application.port.in;

import io.jsonwebtoken.Claims;

import java.time.temporal.TemporalAmount;
import java.util.Map;
import java.util.function.Function;

public interface JwtPort {

    String generateToken(String subject, Map<String, Object> claims, TemporalAmount ttl);

    boolean validateToken(String token);

    boolean validateToken(String token, String subject);

    boolean validateToken(String token, String subject, String tokenType);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

}
