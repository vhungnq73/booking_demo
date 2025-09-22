# Booking Service - Complete (Rebuilt)

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
