package vtrip.booking.booking_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * Default constructor để pass rule AtLeastOneConstructor.
     * Được suppress để tránh PMD.UnnecessaryConstructor.
     */
    @SuppressWarnings("PMD.UnnecessaryConstructor")
    public WebClientConfig() {
        super();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
