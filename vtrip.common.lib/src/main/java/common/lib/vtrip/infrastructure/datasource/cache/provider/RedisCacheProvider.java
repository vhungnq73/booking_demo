package common.lib.vtrip.infrastructure.datasource.cache.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.lib.vtrip.infrastructure.datasource.properties.mapper.RedisConfig;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import common.lib.vtrip.infrastructure.util.Constants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;



@Slf4j
@Service
public class RedisCacheProvider implements IRedisCacheProvider {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisConfig redisConfig;

    public RedisCacheProvider(StringRedisTemplate redisTemplate, RedisConfig redisConfig) {
        this.redisTemplate = redisTemplate;
        this.redisConfig = redisConfig;

        validateConfig();
    }

    private void validateConfig() {
        boolean hasCluster = redisConfig.getCluster() != null
                && redisConfig.getCluster().getNodes() != null
                && !redisConfig.getCluster().getNodes().isEmpty();

        boolean hasHostPort = redisConfig.getHost() != null
                && !redisConfig.getHost().isEmpty()
                && redisConfig.getPort() != null;

        if (!hasCluster && !hasHostPort) {
            throw new IllegalStateException(
                    "Redis configuration is missing. Provide either cluster nodes or host+port."
            );
        }
    }


    private String buildKey(String key) {
        return redisConfig.getKeyPrefix() + key;
    }

    @Override
    public <T> T get(final String key, final Class<T> type) {
        try {
            final String json = redisTemplate.opsForValue().get(key);
            return (json == null) ? null : objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RedisException("Deserialize Redis cache failed", e);
        }
    }

    @Override
    public void set(final String key, final Object value, final long ttlSeconds) {
        try {
            final String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, ttlSeconds, TimeUnit.SECONDS);
        } catch (IOException e) {
            throw new RedisException("Serialize Redis cache failed", e);
        }
    }

    @Override
    public void delete(final String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean exists(final String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public <T> void putHash(final String hashKey, final String field, final T value) {
        try {
            redisTemplate.opsForHash().put(buildKey(hashKey), field, objectMapper.writeValueAsString(value));
        } catch (IOException e) {
            throw new RedisException(e);
        }
    }

    @Override
    public <T> T getHash(final String hashKey, final String field, final Class<T> type) {
        final Object json = redisTemplate.opsForHash().get(buildKey(hashKey), field);
        if (json == null)
        {
            return null;
        }
        try {
            return objectMapper.readValue(json.toString(), type);
        } catch (IOException e) {
            throw new RedisException(e);
        }
    }

    @Override
    public <T> void pushList(final String listKey, final T value) {
        try {
            redisTemplate.opsForList().rightPush(buildKey(listKey), objectMapper.writeValueAsString(value));
        } catch (IOException e) {
            throw new RedisException(e);
        }
    }

    @Override
    public <T> T popListLeft(final String listKey, final Class<T> type) {
        final String json = redisTemplate.opsForList().leftPop(buildKey(listKey));
        if (json == null)
        {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RedisException(e);
        }
    }

    @Override
    public <T> T popListRight(final String listKey, final Class<T> type) {
        final String json = redisTemplate.opsForList().rightPop(buildKey(listKey));
        if (json == null)
        {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RedisException(e);
        }
    }

    @Override
    public <T> void pushListWithTtl(final String listKey, final T value, final long ttlSeconds) {
        try {
            final String json = objectMapper.writeValueAsString(value);
            final long expireAt = System.currentTimeMillis() + ttlSeconds * 1000;
            redisTemplate.opsForZSet().add(buildKey(listKey), json, expireAt);
        } catch (IOException e) {
            throw new RedisException("Serialize Redis list item failed", e);
        }
    }

    @Override
    public <T> T popListLeftWithTtl(final String listKey, final Class<T> type) {
        final String key = buildKey(listKey);
        final long now = System.currentTimeMillis();

        final Set<String> items = redisTemplate.opsForZSet().rangeByScore(key, 0, now, 0, 1);
        if (items == null || items.isEmpty())
        {
            return null;
        }

        final String json = items.iterator().next();
        redisTemplate.opsForZSet().remove(key, json);

        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RedisException("Deserialize Redis list item failed", e);
        }
    }

    @Override
    public <T> List<T> rangeListWithTtl(final String listKey, final Class<T> type, final long start, final long end) {
        final String key = buildKey(listKey);
        final long now = System.currentTimeMillis();
        final Set<String> items = redisTemplate.opsForZSet().rangeByScore(key, 0, now);
        if (items == null)
        {
            return Collections.emptyList();
        }

        final List<T> result = new ArrayList<>();
        for (final String json : items) {
            try {
                result.add(objectMapper.readValue(json, type));
            } catch (IOException e) {
                log.error("Deserialize Redis list item failed", e);
            }
        }
        return result;
    }

    @Override
    public String lock(String lockKey, long leaseTimeMs) {
        while (true) {
            String value = tryLock(lockKey, leaseTimeMs);
            if (value != null) {
                return value;
            }
            try {
                Thread.sleep(Constants.CACHE_TIMEOUT.DEFAULT_SLEEP_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String tryLock(String lockKey, long leaseTimeMs) {
        String key = "lock:" + lockKey;
        String value = UUID.randomUUID().toString();

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, value, leaseTimeMs, TimeUnit.MILLISECONDS);
        if (Boolean.TRUE.equals(success)) {
            return value;
        }
        return null;
    }

    @Override
    public String tryLock(String lockKey, long acquireTimeoutMs, long leaseTimeMs) {
        long end = System.currentTimeMillis() + acquireTimeoutMs;
        while (System.currentTimeMillis() < end) {
            String value = tryLock(lockKey, leaseTimeMs);
            if (value != null) {
                return value;
            }
            try {
                Thread.sleep(Constants.CACHE_TIMEOUT.DEFAULT_SLEEP_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public void unlock(String lockKey, String value) {
        String key = "lock:" + lockKey;
        String lua = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) else return 0 end";

        redisTemplate.execute((RedisCallback<Object>) (connection) ->
                connection.eval(
                        lua.getBytes(StandardCharsets.UTF_8),
                        ReturnType.INTEGER,
                        1,
                        key.getBytes(StandardCharsets.UTF_8),
                        value.getBytes(StandardCharsets.UTF_8)
                )
        );
    }
}
