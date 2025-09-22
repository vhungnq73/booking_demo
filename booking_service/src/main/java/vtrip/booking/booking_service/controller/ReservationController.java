package vtrip.booking.booking_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vtrip.booking.booking_service.job.WakeupCallJob;
import vtrip.booking.booking_service.pubsub.producer.ReservationKafkaProducer;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReservationController implements IReservationOperation {
    private final ReservationKafkaProducer producer;
    private final WakeupCallJob wakeupCallJob;

    public String publishReservation(
            final @PathVariable String id,
            final @RequestBody Object payload
    ) {
        producer.sendReservationEvent(id, payload);
        return "Published reservation " + id;
    }

    @Override
    public String updateReservation(
        final @PathVariable String id,
        final @RequestBody Object payload
    ) {
            producer.sendReservationUpdateEvent(id, payload);
            return "Published update reservation " + id;
        }

    @Override
    public String getReservation(final String id) {
        return id;
    }

    @Override
    public String wakeupCall(final String id) {
        for (int i = 1; i <= 5; i++) {
            wakeupCallJob.runAsyncJob(i);
        }
        return "Finished!";
    }
}
