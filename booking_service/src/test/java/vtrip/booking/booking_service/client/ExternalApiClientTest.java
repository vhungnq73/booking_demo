package vtrip.booking.booking_service.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalApiClientTest {

    @Mock
    private WebClient.Builder builder;

    @Mock
    private WebClient webClient;

    // dùng raw type để tránh vấn đề generic-capture của Mockito
    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ExternalApiClient client;

    @Test
    void testGetExampleData_ok() {
        when(builder.build()).thenReturn(webClient);

        // ép chọn overload uri(String, Object...) bằng Object[] matcher
        doReturn(uriSpec).when(webClient).get();
        doReturn(headersSpec).when(uriSpec)
                .uri(anyString(), org.mockito.ArgumentMatchers.<Object[]>any());
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(Mono.just("{\"id\":1}")).when(responseSpec).bodyToMono(String.class);

        String result = client.getExampleData("1");
        assertEquals("{\"id\":1}", result);
    }

    @Test
    void testGetExampleData_errorFallback() {
        when(builder.build()).thenReturn(webClient);

        doReturn(uriSpec).when(webClient).get();
        doReturn(headersSpec).when(uriSpec)
                .uri(anyString(), org.mockito.ArgumentMatchers.<Object[]>any());
        doReturn(responseSpec).when(headersSpec).retrieve();
        // giả lập lỗi để đi vào onErrorResume -> "{}"
        doReturn(Mono.error(new RuntimeException("boom")))
                .when(responseSpec).bodyToMono(String.class);

        String result = client.getExampleData("1");
        assertEquals("{}", result);
    }
}
