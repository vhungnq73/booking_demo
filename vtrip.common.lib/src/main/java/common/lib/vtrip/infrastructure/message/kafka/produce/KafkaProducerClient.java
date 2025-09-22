package common.lib.vtrip.infrastructure.message.kafka.produce;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaProducerClient {

    private final KafkaTemplate<String, Object> template;

    public CompletableFuture<SendResult<String, Object>> send(String topic, Object value) {
        return template.send(topic, value);
    }

    public CompletableFuture<SendResult<String, Object>> send(String topic, String key, Object value) {
        return template.send(topic, key, value);
    }

    public CompletableFuture<SendResult<String, Object>> sendWithHeaders(String topic,
                                                                         String key,
                                                                         Object value,
                                                                         Map<String, String> headers) {
        return template.executeInTransaction(kt -> {
            var msg = MessageBuilder
                    .withPayload(value)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .setHeader(KafkaHeaders.KEY, key)
                    .copyHeaders(headers)
                    .build();
            return kt.send(msg);
        });
    }
}

