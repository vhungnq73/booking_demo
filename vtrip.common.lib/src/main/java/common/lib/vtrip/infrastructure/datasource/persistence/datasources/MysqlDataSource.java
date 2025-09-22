package common.lib.vtrip.infrastructure.datasource.persistence.datasources;

import com.zaxxer.hikari.HikariConfig;

public class MysqlDataSource extends CommonDataSource {
    public MysqlDataSource() {
        super();
        this.setType(Type.MYSQL);
    }

    public MysqlDataSource(HikariConfig configuration) {
        super(configuration);
        this.setType(Type.MYSQL);
    }
}
