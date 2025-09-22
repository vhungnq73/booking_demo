package vtrip.booking.booking_service.booking.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import vtrip.booking.booking_service.booking.CustomerOrder;
import vtrip.booking.booking_service.booking.Product;
import vtrip.booking.booking_service.booking.repository.OrderRepository;
import vtrip.booking.booking_service.booking.repository.ProductRepository;
import common.lib.vtrip.infrastructure.datasource.cache.provider.IRedisCacheProvider;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        BookingService bookingService = new BookingService(productRepository, orderRepository, redisCache);

        // Given
        Product product = Product.builder()
                .id(1L)
                .sku("SKU1")
                .name("Test Product")
                .price(new BigDecimal("10.00"))
                .stock(5)
                .build();

        when(productRepository.findBySku("SKU1")).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // When
        CustomerOrder order = bookingService.book(Map.of("SKU1", 2));

        // Then
        assertNotNull(order);
        assertEquals(3, product.getStock(), "Stock should be decremented by booked quantity");
        verify(redisCache).set(eq("booking:last_order_size"), eq("1"), anyLong());
        verify(orderRepository).save(order);
    }
}
