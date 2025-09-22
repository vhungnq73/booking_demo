package common.lib.vtrip.infrastructure.datasource.properties.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.lib.vtrip.infrastructure.datasource.properties.dto.ExternalKeyValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.List;

public final class ExternalConfigLoader {
    public static List<ExternalKeyValue> load(final String configUrl) {
        try (InputStream inputStream = new URL(configUrl).openStream()) {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load external config from: " + configUrl, e);
        }
    }
}
