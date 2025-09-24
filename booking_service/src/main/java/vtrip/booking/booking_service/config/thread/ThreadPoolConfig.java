package vtrip.booking.booking_service.config.thread;

import common.lib.vtrip.infrastructure.datasource.properties.mapper.ThreadConfig;
import common.lib.vtrip.infrastructure.worker.ThreadLocalTaskDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class ThreadPoolConfig {

    @Autowired
    private ThreadConfig threadConfig;

    @SuppressWarnings("PMD.UnnecessaryConstructor")
    public ThreadPoolConfig() {
        super();
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        // fallback mặc định nếu maxSize <= 0
        final int poolSize = threadConfig.getMaxSize() > 0 ? threadConfig.getMaxSize() : 5;

        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix("scheduler-");
        scheduler.initialize();
        return scheduler;
    }

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(new ThreadLocalTaskDecorator());

        // fallback mặc định nếu config không hợp lệ
        final int corePoolSize = threadConfig.getCoreSize() > 0 ? threadConfig.getCoreSize() : 2;
        final int maxPoolSize = threadConfig.getMaxSize() > 0 ? threadConfig.getMaxSize() : 5;
        final int queueCapacity = threadConfig.getQueueCapacity() > 0 ? threadConfig.getQueueCapacity() : 100;

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(
                threadConfig.getName() != null ? threadConfig.getName() : "taskExecutor-"
        );
        executor.initialize();
        return executor;
    }
}
