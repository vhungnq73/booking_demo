package vtrip.booking.booking_service.service.greeting;

import vtrip.booking.booking_service.model.GreetingPoint;

import java.util.List;

public interface IGreetingService {
    String greet();
    List<GreetingPoint> getGreetingPoints();
    List<GreetingPoint> cleanGreetingPoints();
    String manualCacheTest();
}
