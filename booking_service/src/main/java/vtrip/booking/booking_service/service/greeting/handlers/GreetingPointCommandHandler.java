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

    private final GreetingPointQueryHandler queryHandler;
    private final GreetingPointRepository repo;

    @Transactional
    public GreetingPoint create(final GreetingPoint greetingPoint) {
        return repo.save(greetingPoint);
    }

    @Transactional
    public void deleteAll() {
        final List<GreetingPoint> list = queryHandler.getGreetingPoints();
        if (!list.isEmpty()) {
            repo.deleteAllInBatch(list);
        }
    }
}
