package common.lib.vtrip.infrastructure.datasource.properties.mapper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfig {

    private Cluster cluster;
    private String host;
    private Integer port;
    private String keyPrefix;

    @Data
    public static class Cluster {
        private List<String> nodes;
    }
}
