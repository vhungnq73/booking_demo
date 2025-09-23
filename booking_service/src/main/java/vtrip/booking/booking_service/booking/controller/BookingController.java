package vtrip.booking.booking_service.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vtrip.booking.booking_service.booking.Product;
import vtrip.booking.booking_service.booking.CustomerOrder;
import vtrip.booking.booking_service.booking.dto.AddProductRequest;
import vtrip.booking.booking_service.booking.dto.BookRequest;
import vtrip.booking.booking_service.booking.service.BookingService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody final AddProductRequest req) {
        Product product = bookingService.addProduct(req.sku(), req.name(), req.price(), req.stock());
        return ResponseEntity.created(URI.create("/api/products/" + product.getId())).body(product);
    }

    @GetMapping("/products")
    public List<Product> search(@RequestParam(name = "search", required = false) final String query) {
        return bookingService.searchProducts(query);
    }

    @PostMapping("/bookings")
    public ResponseEntity<CustomerOrder> book(@RequestBody final BookRequest req) {
        CustomerOrder order = bookingService.book(req.items());
        return ResponseEntity.created(URI.create("/api/orders/" + order.getId())).body(order);
    }

    @GetMapping("/orders/{id}")
    public CustomerOrder get(@PathVariable final long id) {
        return bookingService.getOrder(id);
    }

    // ✅ Endpoint test ExternalApiClient
    @GetMapping("/external-test/{id}")
    public ResponseEntity<String> testExternalApi(@PathVariable final String id) {
        String response = bookingService.demoApiClientCall(id);
        return ResponseEntity.ok(response);
    }
}
