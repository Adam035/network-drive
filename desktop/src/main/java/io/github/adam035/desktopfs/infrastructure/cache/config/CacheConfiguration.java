package io.github.adam035.desktopfs.infrastructure.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@EnableCaching
@Configuration
public class CacheConfiguration {

    @Value("${network-drive.cache.storage-resources.expire-after-write}")
    private long storageResourcesExpireAfterWrite;

    @Value("${network-drive.cache.storage-resources.maximum-size}")
    private long storageResourcesMaximumSize;

    @Value("${network-drive.cache.directory-listings.expire-after-write}")
    private long directoryListingsExpireAfterWrite;

    @Value("${network-drive.cache.directory-listings.maximum-size}")
    private long directoryListingsMaximumSize;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        CaffeineCache storageResources = new CaffeineCache(
                "storageResources",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(storageResourcesExpireAfterWrite))
                        .maximumSize(storageResourcesMaximumSize)
                        .build()
        );

        CaffeineCache directoryListings = new CaffeineCache(
                "directoryListings",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(directoryListingsExpireAfterWrite))
                        .maximumSize(directoryListingsMaximumSize)
                        .build()
        );

        cacheManager.setCaches(List.of(storageResources, directoryListings));

        return cacheManager;
    }

}
