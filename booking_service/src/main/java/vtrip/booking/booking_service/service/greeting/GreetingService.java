package vtrip.booking.booking_service.service.greeting;

import common.lib.vtrip.infrastructure.datasource.cache.provider.IRedisCacheProvider;
import common.lib.vtrip.infrastructure.datasource.properties.mapper.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vtrip.booking.booking_service.model.GreetingPoint;
import vtrip.booking.booking_service.service.greeting.handlers.GreetingPointCommandHandler;
import vtrip.booking.booking_service.service.greeting.handlers.GreetingPointQueryHandler;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GreetingService implements IGreetingService {

    private final GreetingPointCommandHandler greetingPointCommandHandler;
    private final GreetingPointQueryHandler greetingPointQueryHandler;
    private final IRedisCacheProvider redisCacheProvider;
    private final AppConfig appConfig;

    @Override
    public String greet() {
        // Giả sử AppConfig có method getName() trực tiếp
        return "Hello from " + appConfig.getName();
    }

    @Override
    public List<GreetingPoint> getGreetingPoints() {
        return greetingPointQueryHandler.getGreetingPoints();
    }

    @Override
    public List<GreetingPoint> cleanGreetingPoints() {
        greetingPointCommandHandler.deleteAll();
        return greetingPointQueryHandler.getGreetingPoints();
    }

    @Override
    public String manualCacheTest() {
        String key = "greet:key";

        // redisCacheProvider.get trả về String, nên dùng thẳng String
        String cached = redisCacheProvider.get(key, String.class);
        if (cached != null) {
            return cached + " (cache)";
        }

        String value = greet();
        redisCacheProvider.set(key, value, 30);
        return value + " (fresh)";
    }
}
