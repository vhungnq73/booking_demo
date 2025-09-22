package common.lib.vtrip.infrastructure.worker;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ThreadLocalTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        // web thread context (parent thread)
        Map<String, String> contextMap = Optional.ofNullable(MDC.getCopyOfContextMap())
                .orElse(Collections.emptyMap());
        return () -> {
            try {
                MDC.setContextMap(contextMap);
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
