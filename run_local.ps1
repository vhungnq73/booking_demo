#!/usr/bin/env pwsh
docker compose up -d
Start-Sleep -Seconds 5
mvn -DskipTests package
Set-Location booking_service
mvn spring-boot:run
