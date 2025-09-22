package vtrip.booking.booking_service.booking.dto;

import java.util.Map;

// key: sku, value: quantity
public record BookRequest(Map<String, Integer> items) {}
