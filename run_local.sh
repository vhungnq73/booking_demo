#!/usr/bin/env bash
set -euo pipefail
docker compose up -d
echo "Waiting 5s for services..."; sleep 5
mvn -DskipTests package
cd booking_service
mvn spring-boot:run
