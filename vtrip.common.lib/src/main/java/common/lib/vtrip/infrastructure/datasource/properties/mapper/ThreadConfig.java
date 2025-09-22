package common.lib.vtrip.infrastructure.datasource.properties.mapper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadConfig {
    private int coreSize;
    private int maxSize;
    private String name;
    private int queueCapacity;
}
