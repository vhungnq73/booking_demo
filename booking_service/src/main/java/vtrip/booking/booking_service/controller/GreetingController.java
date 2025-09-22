package vtrip.booking.booking_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import vtrip.booking.booking_service.model.GreetingPoint;
import vtrip.booking.booking_service.service.greeting.IGreetingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GreetingController implements IGreetingOperation {
    private final IGreetingService greetingService;

    @Override
    public String greeting() {
        return greetingService.manualCacheTest();
    }

    @Override
    public List<GreetingPoint> greetingPoints() {
        return greetingService.getGreetingPoints();
    }

    @Override
    public List<GreetingPoint> cleanGreetingPoints() {
        return greetingService.cleanGreetingPoints();
    }
}
