package common.lib.vtrip.infrastructure.message.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaCustomProperties {
    private String bootstrapServers;
    private Integer retryPeriodSecond;
    private String prefixKey;
    private int concurrency;
    private long pollTimeout;
    private Map<String, String> consumeTopics = new ConcurrentHashMap<>();
    private Map<String, String> produceTopics = new ConcurrentHashMap<>();
}
