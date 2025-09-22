package common.lib.vtrip.infrastructure.datasource.cache.provider;

public interface IBaseCacheProvider {
    <T> T get(String key, Class<T> type);
    void set(String key, Object value, long ttlSeconds);
    void delete(String key);
    boolean exists(String key);
}
