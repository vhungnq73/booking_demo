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

    @Override
    public String publishReservation(
            final @PathVariable String reservationId,
            final @RequestBody Object payload
    ) {
        producer.sendReservationEvent(reservationId, payload);
        return "Published reservation " + reservationId;
    }

    @Override
    public String updateReservation(
            final @PathVariable String reservationId,
            final @RequestBody Object payload
    ) {
        producer.sendReservationUpdateEvent(reservationId, payload);
        return "Published update reservation " + reservationId;
    }

    @Override
    public String getReservation(final String reservationId) {
        return reservationId;
    }

    @Override
    public String wakeupCall(final String reservationId) {
        for (int index = 1; index <= 5; index++) {
            wakeupCallJob.runAsyncJob(index);
        }
        return "Finished!";
    }
}

