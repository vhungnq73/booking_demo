package vtrip.booking.booking_service.pubsub.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ReservationKafkaConsumer {

    @KafkaListener(
            topics = "dev_kafka_topic_ops_front_office_reservation",
            groupId = "dev_ops_transaction_account",
            containerFactory = "autoCommitFactory",   // ✅ khớp bean trong KafkaConfig
            concurrency = "3"
    )
    public void consumeReservation(final Map<String, Object> message) {
        if (message == null || message.isEmpty()) {
            log.warn("Received empty reservation event (auto commit)");
            return;
        }
        log.info("Received reservation event auto commit: {}", message);
    }

    @KafkaListener(
            topics = "dev_kafka_topic_ops_front_office_reservation_update",
            groupId = "dev_ops_transaction_account",
            containerFactory = "manualCommitFactory",  // ✅ khớp bean trong KafkaConfig
            concurrency = "2"
    )
    public void consumeReservationUpdate(@Payload final Object message, final Acknowledgment acknowledgment) {
        if (message == null) {
            log.warn("Received null update reservation event (manual commit)");
            return;
        }
        try {
            log.info("Received update reservation event manual commit: {}", message);
            acknowledgment.acknowledge();
        } catch (final KafkaException ex) {
            log.error("Error processing reservation update event: {}", message, ex);
        }
    }
}
