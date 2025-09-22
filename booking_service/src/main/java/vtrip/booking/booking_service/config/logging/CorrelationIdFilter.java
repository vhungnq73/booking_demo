package vtrip.booking.booking_service.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {
    private static final String HDR = "X-Request-ID";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String rid = Optional.ofNullable(request.getHeader(HDR)).orElse(UUID.randomUUID().toString());
        MDC.put("requestId", rid);
        response.setHeader(HDR, rid);
        try { filterChain.doFilter(request, response); }
        finally { MDC.clear(); }
    }
}
