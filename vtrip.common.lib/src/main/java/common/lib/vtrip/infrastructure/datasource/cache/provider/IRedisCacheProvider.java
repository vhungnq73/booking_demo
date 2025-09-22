package common.lib.vtrip.infrastructure.datasource.cache.provider;

import java.util.List;

public interface IRedisCacheProvider extends IBaseCacheProvider {
    <T> void putHash(String hashKey, String field, T value);
    <T> T getHash(String hashKey, String field, Class<T> type);
    <T> void pushList(String listKey, T value);
    <T> T popListLeft(String listKey, Class<T> type);
    <T> T popListRight(String listKey, Class<T> type);
    <T> void pushListWithTtl(String listKey, T value, long ttlSeconds);
    <T> T popListLeftWithTtl(String listKey, Class<T> type);
    <T> List<T> rangeListWithTtl(String listKey, Class<T> type, long start, long end);
    String lock(String lockKey, long leaseTimeMs);
    String tryLock(String lockKey, long acquireTimeoutMs, long leaseTimeMs);
    void unlock(String lockKey, String value);
}
