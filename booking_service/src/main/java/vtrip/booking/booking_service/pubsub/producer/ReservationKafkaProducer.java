package vtrip.booking.booking_service.pubsub.producer;

import common.lib.vtrip.infrastructure.message.kafka.produce.KafkaProducerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationKafkaProducer {

    private final KafkaProducerClient producer;

    public void sendReservationEvent(final String resId, final Object payload) {
        log.info("Sending reservation event: id={}, payload={}", resId, payload);

        final String topic = "dev_kafka_topic_ops_front_office_reservation";
        producer.send(topic, resId, payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send message", ex);
                    } else if (log.isInfoEnabled()) {
                        log.info("Message sent successfully to partition {} with offset {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    public void sendReservationUpdateEvent(final String resId, final Object payload) {
        log.info("Sending reservation update event: id={}, payload={}", resId, payload);

        final String topic = "dev_kafka_topic_ops_front_office_reservation_update";
        producer.send(topic, resId, payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send message", ex);
                    } else if (log.isInfoEnabled()) {
                        log.info("Message sent successfully to partition {} with offset {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}

