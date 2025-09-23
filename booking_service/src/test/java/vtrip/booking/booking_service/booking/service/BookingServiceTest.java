package vtrip.booking.booking_service.booking.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import vtrip.booking.booking_service.booking.Product;
import vtrip.booking.booking_service.booking.repository.OrderRepository;
import vtrip.booking.booking_service.booking.repository.ProductRepository;
import common.lib.vtrip.infrastructure.datasource.cache.provider.IRedisCacheProvider;
import vtrip.booking.booking_service.client.ExternalApiClient;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Test
    @DisplayName("BookingService.book() tạo đơn hàng và trừ tồn kho")
    void book_creates_order_and_decrements_stock() {
        // Mock dependencies
        ProductRepository productRepository = mock(ProductRepository.class);
        OrderRepository orderRepository = mock(OrderRepository.class);
        IRedisCacheProvider redisCache = mock(IRedisCacheProvider.class);
        ExternalApiClient externalApiClient = mock(ExternalApiClient.class); // thêm mock mới

        BookingService bookingService = new BookingService(
                productRepository,
                orderRepository,
                redisCache,
                externalApiClient
        );

        // Given
        Product product = Product.builder()
                .id(1L)
                .sku("SKU1")
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .build();

        when(productRepository.findBySku("SKU1")).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        bookingService.book(Map.of("SKU1", 2));

        // Then
        assertEquals(8, product.getStock()); // tồn kho giảm 2
        verify(productRepository).findBySku("SKU1");
        verify(orderRepository).save(any());
        verify(redisCache).set(eq("booking:last_order_size"), eq("1"), eq(300L));
    }
}
