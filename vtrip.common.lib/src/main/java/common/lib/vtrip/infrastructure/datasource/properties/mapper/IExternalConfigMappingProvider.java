package common.lib.vtrip.infrastructure.datasource.properties.mapper;

import java.util.Map;

public interface IExternalConfigMappingProvider {
    default Map<String, String> prefixMapping() {
        return Map.of();
    }

    default Map<String, String> keyMapping() {
        return Map.of();
    }
}
