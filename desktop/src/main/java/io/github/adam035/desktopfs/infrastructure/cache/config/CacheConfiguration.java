package io.github.adam035.desktopfs.infrastructure.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Value("${network-drive.cache.expire-after-write}")
    private long expireAfterWrite;

    @Value("${network-drive.cache.max-size}")
    private long maxSize;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("storageResources");

        manager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(expireAfterWrite))
                        .maximumSize(maxSize)
        );

        return manager;
    }
}
