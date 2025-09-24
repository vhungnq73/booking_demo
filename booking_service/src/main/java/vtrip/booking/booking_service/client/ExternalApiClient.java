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
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public String getExampleData(final String resourceId) {
        String result = "{}"; // Initialize with default value

        try {
            final String apiResponse = webClientBuilder.build()
                    .get()
                    .uri(BASE_URL + "/posts/{id}", resourceId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(ex -> {
                        log.error("Error calling external API", ex);
                        return Mono.just("{}");
                    })
                    .block();

            if (apiResponse != null) {
                result = apiResponse;
            }
        } catch (final Exception ex) {
            log.error("Unexpected error calling external API", ex);
            // result remains as default "{}"
        }

        return result; // Only one return statement
    }
}