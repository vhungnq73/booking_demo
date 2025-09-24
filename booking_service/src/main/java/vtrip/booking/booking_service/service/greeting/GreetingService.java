package vtrip.booking.booking_service.service.greeting;

import common.lib.vtrip.infrastructure.datasource.cache.provider.IRedisCacheProvider;
import common.lib.vtrip.infrastructure.datasource.properties.mapper.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vtrip.booking.booking_service.model.GreetingPoint;
import vtrip.booking.booking_service.service.greeting.handlers.GreetingPointCommandHandler;
import vtrip.booking.booking_service.service.greeting.handlers.GreetingPointQueryHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GreetingService implements IGreetingService {

    private final GreetingPointCommandHandler cmdHandler;
    private final GreetingPointQueryHandler queryHandler;
    private final IRedisCacheProvider cache;
    private final AppConfig cfg;

    @Override
    public String greet() {
        return "Hello from " + cfg.getName();
    }

    @Override
    public List<GreetingPoint> getGreetingPoints() {
        return queryHandler.getGreetingPoints();
    }

    @Override
    public List<GreetingPoint> cleanGreetingPoints() {
        cmdHandler.deleteAll();
        return queryHandler.getGreetingPoints();
    }

    @Override
    public String manualCacheTest() {
        final String key = "greet:key";
        final String cached = cache.get(key, String.class);
        final String result;
        if (cached != null) {
            result = cached + " (cache)";
        } else {
            final String value = greet();
            cache.set(key, value, 30);
            result = value + " (fresh)";
        }
        return result;
    }

}

