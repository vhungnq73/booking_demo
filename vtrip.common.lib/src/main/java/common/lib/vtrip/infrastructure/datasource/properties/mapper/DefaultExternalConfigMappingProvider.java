package common.lib.vtrip.infrastructure.datasource.properties.mapper;

import java.util.Map;

public class DefaultExternalConfigMappingProvider implements IExternalConfigMappingProvider {
    public DefaultExternalConfigMappingProvider() {}

    @Override
    public Map<String, String> prefixMapping() {
        return Map.of();
    }

    @Override
    public Map<String, String> keyMapping() {
        return Map.of();
    }
}
