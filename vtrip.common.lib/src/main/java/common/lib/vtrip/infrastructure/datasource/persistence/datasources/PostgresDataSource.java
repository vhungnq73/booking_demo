package common.lib.vtrip.infrastructure.datasource.persistence.datasources;

import com.zaxxer.hikari.HikariConfig;

public class PostgresDataSource extends CommonDataSource {
    public PostgresDataSource() {
        super();
        this.setType(Type.POSTGRESQL);
    }

    public PostgresDataSource(HikariConfig configuration) {
        super(configuration);
        this.setType(Type.POSTGRESQL);
    }
}
