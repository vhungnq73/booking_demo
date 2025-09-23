package vtrip.booking.booking_service.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtrip.booking.booking_service.booking.CustomerOrder;
import vtrip.booking.booking_service.booking.OrderItem;
import vtrip.booking.booking_service.booking.Product;
import vtrip.booking.booking_service.booking.repository.OrderRepository;
import vtrip.booking.booking_service.booking.repository.ProductRepository;
import vtrip.booking.booking_service.client.ExternalApiClient;
import common.lib.vtrip.infrastructure.datasource.cache.provider.IRedisCacheProvider;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling booking operations:
 * - Add/search products
 * - Create orders
 * - Get order details
 * - Call external APIs
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final IRedisCacheProvider redisCache;
    private final ExternalApiClient externalApiClient;

    /**
     * Add new product
     */
    @Transactional
    public Product addProduct(final String sku, final String name, final BigDecimal price, final int stock) {
        final Product product = Product.builder()
                .sku(sku)
                .name(name)
                .price(price)
                .stock(stock)
                .build();
        return productRepository.save(product);
    }

    /**
     * Search product by name (case insensitive)
     */
    public List<Product> searchProducts(final String query) {
        if (query == null || query.isBlank()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    /**
     * Book products and create order
     */
    @Transactional
    public CustomerOrder book(final Map<String, Integer> itemsBySku) {
        final List<OrderItem> items = new ArrayList<>();
        final CustomerOrder order = CustomerOrder.builder()
                .createdAt(OffsetDateTime.now())
                .items(items)
                .build();

        for (Map.Entry<String, Integer> entry : itemsBySku.entrySet()) {
            final Product product = productRepository.findBySku(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + entry.getKey()));

            final int quantity = entry.getValue();
            if (product.getStock() < quantity) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getSku());
            }

            product.setStock(product.getStock() - quantity);
            final OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .build();
            items.add(orderItem);
        }

        // save metric to Redis
        redisCache.set("booking:last_order_size", String.valueOf(items.size()), 300L);

        return orderRepository.save(order);
    }

    /**
     * Get order by id
     */
    public CustomerOrder getOrder(final long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("Order not found with id: " + orderId)
        );
    }

    /**
     * Example calling external API
     */
    public String demoApiClientCall(final String id) {
        return externalApiClient.getExampleData(id);
    }
}
