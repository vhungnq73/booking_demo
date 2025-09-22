package vtrip.booking.booking_service.service.greeting.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vtrip.booking.booking_service.model.GreetingPoint;
import vtrip.booking.booking_service.repository.GreetingPointRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GreetingPointCommandHandler {
    private final GreetingPointQueryHandler greetingPointQueryHandler;
    private final GreetingPointRepository greetingPointRepository;

    @Transactional
    public GreetingPoint create(final GreetingPoint greetingPoint) {
        return greetingPointRepository.save(greetingPoint);
    }

    @Transactional
    public void deleteAll() {
        final List<GreetingPoint> greetingPoints = greetingPointQueryHandler.getGreetingPoints();
        greetingPointRepository.deleteAllInBatch(greetingPoints);
    }
}
