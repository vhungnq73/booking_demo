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

PMD is enabled (maven-pmd-plugin). Logging JSON + X-Request-ID configured.

# booking_demo
Booking demo project for IntelliJ test
6a6d74989bdae32a577b9dc354ac6362a8c409e9
    