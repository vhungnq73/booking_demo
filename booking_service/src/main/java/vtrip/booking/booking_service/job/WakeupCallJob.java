package vtrip.booking.booking_service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WakeupCallJob {
    @Async("taskExecutor")
    @SuppressWarnings("PMD.DoNotUseThreads")
    public void runAsyncJob(final int jobId) {
        log.info("Start call {}", jobId);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Done call {}", jobId);
    }
}
