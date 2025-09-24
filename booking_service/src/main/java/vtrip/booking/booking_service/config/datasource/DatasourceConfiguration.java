package vtrip.booking.booking_service.config.datasource;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class DatasourceConfiguration {

    /**
     * Default constructor để pass rule AtLeastOneConstructor.
     * Được suppress để tránh bị PMD.UnnecessaryConstructor bắt lỗi.
     */
    @SuppressWarnings("PMD.UnnecessaryConstructor")
    public DatasourceConfiguration() {
        super();
    }
}
