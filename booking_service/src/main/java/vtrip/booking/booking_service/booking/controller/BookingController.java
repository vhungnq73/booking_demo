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
    public ResponseEntity<Product> addProduct(@RequestBody AddProductRequest req) {
        Product p = bookingService.addProduct(req.sku(), req.name(), req.price(), req.stock());
        return ResponseEntity.created(URI.create("/api/products/" + p.getId())).body(p);
    }

    @GetMapping("/products")
    public List<Product> search(@RequestParam(name="search", required = false) String q) {
        return bookingService.searchProducts(q);
    }

    @PostMapping("/bookings")
    public ResponseEntity<CustomerOrder> book(@RequestBody BookRequest req) {
        CustomerOrder o = bookingService.book(req.items());
        return ResponseEntity.created(URI.create("/api/orders/" + o.getId())).body(o);
    }

    @GetMapping("/orders/{id}")
    public CustomerOrder get(@PathVariable long id) {
        return bookingService.getOrder(id);
    }
}
