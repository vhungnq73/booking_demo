package vtrip.booking.booking_service.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtrip.booking.booking_service.booking.CustomerOrder;
import vtrip.booking.booking_service.booking.OrderItem;
import vtrip.booking.booking_service.booking.Product;
import vtrip.booking.booking_service.booking.repository.OrderRepository;
import vtrip.booking.booking_service.booking.repository.ProductRepository;
import common.lib.vtrip.infrastructure.datasource.cache.provider.IRedisCacheProvider; // dùng trực tiếp provider

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final IRedisCacheProvider redisCache; // dùng trực tiếp provider

    @Transactional
    public Product addProduct(String sku, String name, BigDecimal price, int stock) {
        Product p = Product.builder()
                .sku(sku)
                .name(name)
                .price(price)
                .stock(stock)
                .build();
        return productRepository.save(p);
    }

    public List<Product> searchProducts(String q) {
        if (q == null || q.isBlank()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCase(q);
    }

    @Transactional
    public CustomerOrder book(Map<String, Integer> itemsBySku) {
        List<OrderItem> items = new ArrayList<>();
        CustomerOrder order = CustomerOrder.builder()
                .createdAt(OffsetDateTime.now())
                .items(items)
                .build();

        for (Map.Entry<String, Integer> e : itemsBySku.entrySet()) {
            Product p = productRepository.findBySku(e.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + e.getKey()));

            int qty = e.getValue();
            if (p.getStock() < qty) {
                throw new IllegalArgumentException("Insufficient stock for " + p.getSku());
            }

            p.setStock(p.getStock() - qty);
            OrderItem it = OrderItem.builder()
                    .order(order)
                    .product(p)
                    .quantity(qty)
                    .unitPrice(p.getPrice())
                    .build();
            items.add(it);
        }

        // Lưu metric vào Redis
        redisCache.set("booking:last_order_size", String.valueOf(items.size()), 300L);

        return orderRepository.save(order);
    }

    public CustomerOrder getOrder(long id) {
        return orderRepository.findById(id).orElseThrow();
    }
}
