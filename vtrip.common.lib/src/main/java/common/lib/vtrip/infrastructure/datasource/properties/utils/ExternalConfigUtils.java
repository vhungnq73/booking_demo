package common.lib.vtrip.infrastructure.datasource.properties.utils;

import common.lib.vtrip.infrastructure.datasource.properties.constant.ExternalConfigConstants;
import io.micrometer.common.util.StringUtils;

import java.util.Optional;

public final class ExternalConfigUtils {
    private static final String ENV_CONFIG_URL = ExternalConfigConstants.ENV_CONFIG_URL;
    public static Optional<String>  getExternalConfigUrl() {
        final String url = System.getenv(ENV_CONFIG_URL);
        return StringUtils.isNotBlank(url) ? Optional.of(url) : Optional.empty();
    }
}
