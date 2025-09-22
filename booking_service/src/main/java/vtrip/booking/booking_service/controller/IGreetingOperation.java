package vtrip.booking.booking_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import vtrip.booking.booking_service.model.GreetingPoint;

import java.util.List;

/*
* https://www.baeldung.com/spring-interface-driven-controllers
* */
public interface IGreetingOperation {
    @PostMapping(path ="/greeting")
    @ResponseStatus(HttpStatus.CREATED)
    String greeting();

    @GetMapping(path ="/greeting/points")
    @ResponseStatus(HttpStatus.OK)
    List<GreetingPoint> greetingPoints();

    @DeleteMapping(path ="/greeting/points")
    @ResponseStatus(HttpStatus.OK)
    List<GreetingPoint> cleanGreetingPoints();
}
