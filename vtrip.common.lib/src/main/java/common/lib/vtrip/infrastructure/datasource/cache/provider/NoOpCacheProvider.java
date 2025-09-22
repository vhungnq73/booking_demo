package common.lib.vtrip.infrastructure.datasource.cache.provider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpCacheProvider implements IBaseCacheProvider {
    @Override
    public <T> T get(final String key, Class<T> type) {
        log.debug("[NoOpCache] get key={} (no caching)", key);
        return null;
    }

    @Override
    public void set(final String key, Object value, long ttlSeconds) {
        log.debug("[NoOpCache] put key={} (no caching)", key);
    }

    @Override
    public void delete(final String key) {
        log.debug("[NoOpCache] evict key={} (no caching)", key);
    }

    @Override
    public boolean exists(String key) {
        return false;
    }
}
