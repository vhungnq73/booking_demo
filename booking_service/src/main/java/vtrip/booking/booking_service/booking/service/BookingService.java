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

@Service
@RequiredArgsConstructor
public class BookingService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final IRedisCacheProvider redisCache;
    private final ExternalApiClient externalApiClient;

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

    public List<Product> searchProducts(final String query) {
        final List<Product> result;
        if (query == null || query.isBlank()) {
            result = productRepository.findAll();
        } else {
            result = productRepository.findByNameContainingIgnoreCase(query);
        }
        return result;
    }

    /**
     * Tạo OrderItem cho từng dòng đặt hàng.
     * Suppress rule vì nghiệp vụ buộc phải khởi tạo object cho mỗi item.
     */
    private OrderItem createOrderItem(final CustomerOrder order, final Product product, final int quantity) {
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPrice())
                .build();
    }

    @Transactional
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public CustomerOrder book(final Map<String, Integer> itemsBySku) {
        final List<OrderItem> items = new ArrayList<>();
        final CustomerOrder order = CustomerOrder.builder()
                .createdAt(OffsetDateTime.now())
                .items(items)
                .build();

        for (final Map.Entry<String, Integer> entry : itemsBySku.entrySet()) {
            final Product product = productRepository.findBySku(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + entry.getKey()));

            final int quantity = entry.getValue();
            if (product.getStock() < quantity) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getSku());
            }

            product.setStock(product.getStock() - quantity);
            items.add(createOrderItem(order, product, quantity)); // object được tạo trong helper (đã suppress)
        }

        redisCache.set("booking:last_order_size", String.valueOf(items.size()), 300L);
        return orderRepository.save(order);
    }

    public CustomerOrder getOrder(final long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("Order not found with id: " + orderId)
        );
    }

    public String demoApiClientCall(final String requestId) {
        return externalApiClient.getExampleData(requestId);
    }
}