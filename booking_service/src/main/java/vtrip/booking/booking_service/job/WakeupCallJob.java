package vtrip.booking.booking_service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WakeupCallJob {

    // constructor mặc định để pass AtLeastOneConstructor
    @SuppressWarnings("PMD.UnnecessaryConstructor")
    public WakeupCallJob() {
        super();
    }

    @Async("taskExecutor")
    public void runAsyncJob(final int jobId) {
        log.info("Start call {}", jobId);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        log.info("Done call {}", jobId);
    }
}
