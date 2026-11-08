package io.github.adam035.networkdrive.infrastructure.redis.adapter;

import io.github.adam035.networkdrive.application.dto.OAuth2Attempt;
import io.github.adam035.networkdrive.application.port.AuthorizationCodeStorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisAuthorizationCodeStoreAdapter implements AuthorizationCodeStorePort {

    private static final String PREFIX = "oauth2:code:";

    private static final Duration TTL = Duration.ofMinutes(2);

    private final RedisTemplate<String, OAuth2Attempt> redisTemplate;


    @Override
    public void put(String key, OAuth2Attempt value) {
        redisTemplate.opsForValue().set(PREFIX + key, value, TTL);
    }

    @Override
    public Optional<OAuth2Attempt> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + key));
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(PREFIX + key);
    }

}
