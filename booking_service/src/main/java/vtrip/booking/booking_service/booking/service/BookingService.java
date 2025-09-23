package vtrip.booking.booking_service.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtrip.booking.booking_service.booking.CustomerOrder;
import vtrip.booking.booking_service.booking.OrderItem;
import vtrip.booking.booking_service.booking.Product;
import vtrip.booking.booking_service.booking.repository.OrderRepository;
import vtrip.booking.booking_service.booking.repository.ProductRepository;
import common.lib.vtrip.infrastructure.datasource.cache.provider.IRedisCacheProvider;
import vtrip.booking.booking_service.client.ExternalApiClient;

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
    private final IRedisCacheProvider redisCache;
    private final ExternalApiClient externalApiClient;

    @Transactional
    public Product addProduct(final String sku, final String name, final BigDecimal price, final int stock) {
        Product product = Product.builder()
                .sku(sku)
                .name(name)
                .price(price)
                .stock(stock)
                .build();
        return productRepository.save(product);
    }

    public List<Product> searchProducts(final String query) {
        if (query == null || query.isBlank()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    @Transactional
    public CustomerOrder book(final Map<String, Integer> itemsBySku) {
        List<OrderItem> items = new ArrayList<>();
        CustomerOrder order = CustomerOrder.builder()
                .createdAt(OffsetDateTime.now())
                .items(items)
                .build();

        for (Map.Entry<String, Integer> entry : itemsBySku.entrySet()) {
            Product product = productRepository.findBySku(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + entry.getKey()));

            int quantity = entry.getValue();
            if (product.getStock() < quantity) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getSku());
            }

            product.setStock(product.getStock() - quantity);
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .build();
            items.add(orderItem);
        }

        // Lưu metric vào Redis
        redisCache.set("booking:last_order_size", String.valueOf(items.size()), 300L);

        return orderRepository.save(order);
    }

    public CustomerOrder getOrder(final long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Order not found with id: " + id)
        );
    }

    // Demo gọi external API
    public String demoApiClientCall(final String id) {
        return externalApiClient.getExampleData(id);
    }
}
