package common.lib.vtrip.infrastructure.datasource.properties.mapper;

import common.lib.vtrip.infrastructure.datasource.properties.dto.ExternalKeyValue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ExternalConfigMapper {

    public static Map<String, Object> map(
            final List<ExternalKeyValue> keyValues,
            final Map<String, String> prefixMapping,
            final Map<String, String> keyMapping) {

        if (keyValues == null) {
            return Map.of();
        }

        final Map<String, Object> result = new ConcurrentHashMap<>();

        for (final ExternalKeyValue kv : keyValues) {
            if (kv == null || kv.getKey() == null) {
                continue;
            }

            final String originalKey = kv.getKey();
            String mappedKey;

            // Check keyMapping
            if (keyMapping != null && keyMapping.containsKey(originalKey)) {
                mappedKey = keyMapping.get(originalKey);
            }
            // Check prefixMapping
            else if (prefixMapping != null) {
                final String matchedPrefix = prefixMapping.keySet().stream()
                        .filter(originalKey::startsWith)
                        .findFirst()
                        .orElse(null);
                if (matchedPrefix != null) {
                    mappedKey = prefixMapping.get(matchedPrefix) + originalKey.substring(matchedPrefix.length());
                } else {
                    mappedKey = originalKey;
                }
            } else {
                mappedKey = originalKey;
            }

            // Relaxed binding
            mappedKey = mappedKey.replace("_", ".").replace("-", ".").toLowerCase();

            result.put(mappedKey, kv.getValue());
        }

        return result;
    }

    public static Map<String, Object> map(final List<ExternalKeyValue> keyValues, final Map<String, String> prefixMapping) {
        return map(keyValues, prefixMapping, Map.of());
    }

    public static Map<String, Object> map(final List<ExternalKeyValue> keyValues) {
        return map(keyValues, Map.of(), Map.of());
    }
}
