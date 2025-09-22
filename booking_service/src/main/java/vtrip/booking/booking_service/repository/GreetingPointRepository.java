package vtrip.booking.booking_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vtrip.booking.booking_service.model.GreetingPoint;

@Repository
public interface GreetingPointRepository extends JpaRepository<GreetingPoint, Integer> {
}
