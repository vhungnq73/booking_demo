package vtrip.booking.booking_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface IReservationOperation {
    @PostMapping(path ="/{id}/publish")
    @ResponseStatus(HttpStatus.OK)
    String publishReservation(@PathVariable String id, @RequestBody Object payload);

    @PostMapping(path ="/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    String updateReservation(@PathVariable String id, @RequestBody Object payload);

    @GetMapping(path ="/{id}")
    @ResponseStatus(HttpStatus.OK)
    String getReservation(@PathVariable String id);

    @PostMapping(path ="/{id}/wakeup-call")
    @ResponseStatus(HttpStatus.OK)
    String wakeupCall(@PathVariable String id);
}
