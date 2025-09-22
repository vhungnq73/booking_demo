package vtrip.booking.booking_service.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vtrip.booking.booking_service.booking.CustomerOrder;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
}