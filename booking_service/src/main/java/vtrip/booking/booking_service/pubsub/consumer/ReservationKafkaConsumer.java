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
            containerFactory="autoCommitListenerFactory",
            concurrency = "3"
    )
    public void consumeReservation(final Map<String, Object> message) {
        if (log.isInfoEnabled()) {
            log.info("Received reservation event auto commit: {}", message);
        }
    }

    @KafkaListener(
            topics = "dev_kafka_topic_ops_front_office_reservation_update",
            groupId = "dev_ops_transaction_account",
            containerFactory="manualCommitListenerFactory",
            concurrency = "2"
    )
    /*@RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            dltTopicSuffix = "_dlt",
            autoCreateTopics = "false"
    )*/
    public void consumeReservationUpdate(@Payload final Object message, final Acknowledgment acknowledgment) {
        try {
            if (log.isInfoEnabled()) {
                log.info("Received update reservation event manual commit: {}", message);
            }
            // commit offset
            acknowledgment.acknowledge();

        } catch (KafkaException e) {
            log.error("Error processing message", e);
        }
    }
}
