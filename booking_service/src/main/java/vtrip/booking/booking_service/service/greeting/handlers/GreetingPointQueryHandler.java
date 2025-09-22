package vtrip.booking.booking_service.service.greeting.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vtrip.booking.booking_service.model.GreetingPoint;
import vtrip.booking.booking_service.repository.GreetingPointRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GreetingPointQueryHandler {

    private final GreetingPointRepository greetingPointRepository;

    @Transactional(readOnly = true)
    public List<GreetingPoint> getGreetingPoints() {
        return greetingPointRepository.findAll();
    }
}
