package vtrip.booking.booking_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalApiClient {

    private final WebClient.Builder webClientBuilder;

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com"; // ví dụ external API

    public String getExampleData(final String resourceId) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(BASE_URL + "/posts/{id}", resourceId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(ex -> {
                        log.error("Error calling external API", ex);
                        return Mono.just("{}");
                    })
                    .block();
        } catch (final Exception ex) {
            log.error("Unexpected error calling external API", ex);
            return "{}";
        }
    }
}
