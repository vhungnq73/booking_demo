package vtrip.booking.booking_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface IReservationOperation {
    @PostMapping(path ="/{reservationId}/publish")
    @ResponseStatus(HttpStatus.OK)
    String publishReservation(@PathVariable String reservationId, @RequestBody Object payload);

    @PostMapping(path ="/{reservationId}/update")
    @ResponseStatus(HttpStatus.OK)
    String updateReservation(@PathVariable String reservationId, @RequestBody Object payload);

    @GetMapping(path ="/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    String getReservation(@PathVariable String reservationId);

    @PostMapping(path ="/{reservationId}/wakeup-call")
    @ResponseStatus(HttpStatus.OK)
    String wakeupCall(@PathVariable String reservationId);
}

