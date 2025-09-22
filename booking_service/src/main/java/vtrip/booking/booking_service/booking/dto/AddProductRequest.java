package vtrip.booking.booking_service.booking.dto;

import java.math.BigDecimal;

public record AddProductRequest(String sku, String name, BigDecimal price, Integer stock) {}
