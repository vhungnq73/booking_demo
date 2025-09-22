package vtrip.booking.booking_service.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vtrip.booking.booking_service.booking.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByNameContainingIgnoreCase(String name);
}