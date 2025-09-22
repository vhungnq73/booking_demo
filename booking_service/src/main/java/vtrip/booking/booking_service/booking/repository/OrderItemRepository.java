package vtrip.booking.booking_service.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vtrip.booking.booking_service.booking.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}