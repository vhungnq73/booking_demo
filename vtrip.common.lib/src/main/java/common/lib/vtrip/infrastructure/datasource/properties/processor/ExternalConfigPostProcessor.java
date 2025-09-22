package common.lib.vtrip.infrastructure.datasource.properties.processor;

import common.lib.vtrip.infrastructure.datasource.properties.mapper.ExternalConfigMapper;
import common.lib.vtrip.infrastructure.datasource.properties.mapper.IExternalConfigMappingProvider;
import common.lib.vtrip.infrastructure.datasource.properties.utils.ExternalConfigUtils;
import common.lib.vtrip.infrastructure.datasource.properties.dto.ExternalKeyValue;
import common.lib.vtrip.infrastructure.datasource.properties.constant.ExternalConfigConstants;
import common.lib.vtrip.infrastructure.datasource.properties.loader.ExternalConfigLoader;
import org.springframework.boot.SpringApplication;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.SpringFactoriesLoader;


public class ExternalConfigPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment environment, SpringApplication application) {
        final Optional<String> configUrlOpt = ExternalConfigUtils.getExternalConfigUrl();
        if (configUrlOpt.isEmpty()) {
            return;
        }

        List<IExternalConfigMappingProvider> providers = SpringFactoriesLoader.loadFactories(
                IExternalConfigMappingProvider.class,
                getClass().getClassLoader()
        );

        Map<String, String> combinedPrefixMapping = new ConcurrentHashMap<>();
        Map<String, String> combinedKeyMapping = new ConcurrentHashMap<>();

        for (IExternalConfigMappingProvider provider : providers) {
            combinedPrefixMapping.putAll(provider.prefixMapping());
            combinedKeyMapping.putAll(provider.keyMapping());
        }

        final List<ExternalKeyValue> rawList = ExternalConfigLoader.load(configUrlOpt.get());
        Map<String, Object> mapped = ExternalConfigMapper.map(rawList, combinedPrefixMapping, combinedKeyMapping);

        addConfigToEnvironment(environment, mapped);
    }

    private void addConfigToEnvironment(final ConfigurableEnvironment environment,
                                        final Map<String, Object> config) {
        final MutablePropertySources propertySources = getPropertySources(environment);
        propertySources.addFirst(new MapPropertySource(ExternalConfigConstants.EXTERNAL_CONFIG, config));
        environment.addActiveProfile(ExternalConfigConstants.EXTERNAL_CONFIG);
    }

    private MutablePropertySources getPropertySources(final ConfigurableEnvironment environment) {
        return environment.getPropertySources();
    }
}