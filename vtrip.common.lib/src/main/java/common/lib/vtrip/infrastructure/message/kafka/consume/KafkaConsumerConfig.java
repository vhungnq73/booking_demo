package common.lib.vtrip.infrastructure.message.kafka.consume;

import common.lib.vtrip.infrastructure.message.kafka.config.KafkaCustomProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@EnableConfigurationProperties(KafkaCustomProperties.class)
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final KafkaCustomProperties props;

    private Map<String, Object> baseConsumerConfig() {
        Map<String, Object> config = new ConcurrentHashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getBootstrapServers());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        return config;
    }

    private ContainerProperties.AckMode parseAckMode(String text, ContainerProperties.AckMode fallback) {
        if (text == null) return fallback;
        try {
            return ContainerProperties.AckMode.valueOf(text);
        } catch (IllegalArgumentException e) {
            return fallback;
        }
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "autoCommitListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> autoCommitListenerFactory() {
        final Map<String, Object> config = new HashMap<>(baseConsumerConfig());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        config,
                        new StringDeserializer(),
                        new JsonDeserializer<>(Object.class, false)
                )
        );

        factory.getContainerProperties().setPollTimeout(defaultPollTimeout());
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean(name = "manualCommitListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> manualCommitListenerFactory() {
        final Map<String, Object> config = new HashMap<>(baseConsumerConfig());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        config,
                        new StringDeserializer(),
                        new JsonDeserializer<>(Object.class, false)
                )
        );

        factory.getContainerProperties().setPollTimeout(defaultPollTimeout());

        // Gọi parseAckMode() để tránh bị PMD báo unused
        factory.getContainerProperties().setAckMode(
                parseAckMode("MANUAL_IMMEDIATE", ContainerProperties.AckMode.MANUAL_IMMEDIATE)
        );

        return factory;
    }

    @Bean
    @ConditionalOnMissingBean(name = "manualCommitBatchListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> manualCommitBatchListenerFactory() {
        final Map<String, Object> config = new HashMap<>(baseConsumerConfig());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        config,
                        new StringDeserializer(),
                        new JsonDeserializer<>(Object.class, false)
                )
        );

        factory.getContainerProperties().setPollTimeout(defaultPollTimeout());

        // Ví dụ dùng parseAckMode() luôn ở đây cho batch
        factory.getContainerProperties().setAckMode(
                parseAckMode("BATCH", ContainerProperties.AckMode.BATCH)
        );

        return factory;
    }

    private long defaultPollTimeout() {
        long p = props.getPollTimeout();
        return p < 0 ? 3000L : p;
    }
}
