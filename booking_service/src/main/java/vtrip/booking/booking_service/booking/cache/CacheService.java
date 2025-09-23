package vtrip.booking.booking_service.booking.cache;

import common.lib.vtrip.infrastructure.datasource.cache.provider.IRedisCacheProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Simple cache wrapper for Redis
 */
@Service
@RequiredArgsConstructor
public class CacheService {

    private final IRedisCacheProvider redisCache;

    public void set(final String key, final String value, final long ttlSeconds) {
        redisCache.set(key, value, ttlSeconds);
    }

    public Optional<String> get(final String key) {
        // Giả sử IRedisCacheProvider.get cần 2 tham số: key + class type
        final String val = redisCache.get(key, String.class);
        return Optional.ofNullable(val);
    }

    public void delete(final String key) {
        redisCache.delete(key);
    }
}
