package vtrip.booking.booking_service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ReportJob {

    // constructor mặc định để pass AtLeastOneConstructor
    @SuppressWarnings("PMD.UnnecessaryConstructor")
    public ReportJob() {
        super();
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void generateReport() {
        if (log.isInfoEnabled()) {
            final LocalDateTime now = LocalDateTime.now();
            log.info("{} - generateReport() running", now);
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void cleanUp() {
        if (log.isInfoEnabled()) {
            final LocalDateTime now = LocalDateTime.now();
            log.info("{} - cleanUp() running", now);
        }
    }
}
