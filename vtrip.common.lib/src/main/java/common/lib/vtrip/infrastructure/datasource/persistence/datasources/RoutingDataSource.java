package common.lib.vtrip.infrastructure.datasource.persistence.datasources;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (readOnly) {
            return DataSourceType.READ;
        } else {
            return DataSourceType.WRITE;
        }
    }

    public enum DataSourceType {
        READ,
        WRITE
    }

    public static RoutingDataSource fromDataSource(DataSource write, DataSource read) {
        RoutingDataSource dataSource = new RoutingDataSource();
        Map<Object, Object> map = new HashMap<>();
        Optional.ofNullable(write).ifPresentOrElse(w -> map.put(DataSourceType.WRITE, w),() -> {
            throw new DataSourceLookupFailureException("Database connection configuration error");
        });
        Optional.ofNullable(read).ifPresent(r -> map.put(DataSourceType.READ, r));

        dataSource.setTargetDataSources(map);
        dataSource.setDefaultTargetDataSource(map.get(DataSourceType.WRITE));
        dataSource.afterPropertiesSet();
        return dataSource;
    }
}
