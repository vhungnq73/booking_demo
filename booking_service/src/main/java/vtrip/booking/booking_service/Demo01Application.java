package vtrip.booking.booking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "common.lib.vtrip",
        "vtrip.booking.booking_service",
})
@EnableKafka
@EnableScheduling
public class Demo01Application {
    public static void main(final String[] args) {
        SpringApplication.run(Demo01Application.class, args);
    }
}
