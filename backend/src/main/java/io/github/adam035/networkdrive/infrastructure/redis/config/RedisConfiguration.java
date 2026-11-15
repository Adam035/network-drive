package io.github.adam035.networkdrive.infrastructure.redis.config;

import io.github.adam035.networkdrive.application.dto.OAuth2Attempt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean
    RedisTemplate<String, OAuth2Attempt> oauth2AttemptRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, OAuth2Attempt> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(OAuth2Attempt.class));

        return redisTemplate;
    }

}
