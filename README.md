# Booking Service - Complete (Rebuilt)

Booking demo project for IntelliJ test

## Quick Start
```bash
docker compose up -d
mvn -DskipTests package
cd booking_service
mvn spring-boot:run

```

Services:
- Postgres: localhost:5432 / booking_db / postgres / postgres
- Kafka (Redpanda): localhost:9092
- Redis: localhost:6379
  
API Endpoints:

- POST /api/products – Thêm sản phẩm mới

- GET /api/products?q= – Tìm kiếm sản phẩm

- POST /api/bookings – Tạo booking mới

- GET /api/bookings/{id} – Xem chi tiết booking
- 
Ví Dụ Request:
```bash
POST /api/products
Content-Type: application/json

{
"name": "Vinpearl Nha Trang Resort",
"description": "Phòng nghỉ view biển, bao gồm buffet sáng",
"price": 2500000,
"stock": 10,
"sku": "VINPEARL-NT-004"
}
```
PMD is enabled (maven-pmd-plugin). Logging JSON + X-Request-ID configured.

# booking_demo
Booking demo project for IntelliJ test
6a6d74989bdae32a577b9dc354ac6362a8c409e9
    