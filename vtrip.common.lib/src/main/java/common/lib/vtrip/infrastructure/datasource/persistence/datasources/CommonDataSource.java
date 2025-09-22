package common.lib.vtrip.infrastructure.datasource.persistence.datasources;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
@Setter
public class CommonDataSource extends HikariDataSource {

    private Type type;
    private String host;
    private String port;
    private String database;
    private String options;

    public CommonDataSource() {
        super();
    }

    public CommonDataSource(HikariConfig configuration) {
        super(configuration);
    }

    @PostConstruct
    public void setup() {
        this.setJdbcUrl(null);

        //  Nếu chưa set type thì mặc định dùng POSTGRESQL
        if (Objects.isNull(this.getType())) {
            this.setType(Type.POSTGRESQL);
        }

        if (Objects.nonNull(this.getType())) {
            this.setDriverClassName(this.getType().getDriverClassName());
            if (StringUtils.isNotBlank(this.getHost())) {
                // build the JDBC URL based on the type
                StringBuilder jdbcUrl = new StringBuilder("jdbc:");
                jdbcUrl.append(this.getType().getValue())
                        .append("://")
                        .append(this.getHost());
                if (StringUtils.isNotBlank(this.getPort())) {
                    jdbcUrl.append(":").append(this.getPort());
                }
                if (StringUtils.isNotBlank(this.getDatabase())) {
                    jdbcUrl.append("/").append(this.getDatabase());
                }
                if (StringUtils.isNotBlank(this.getOptions())) {
                    jdbcUrl.append("?").append(this.getOptions());
                }
                this.setJdbcUrl(jdbcUrl.toString());
            }
        }
    }


    @Getter
    public enum Type {
        MYSQL("mysql", "com.mysql.cj.jdbc.Driver"),
        POSTGRESQL("postgresql", "org.postgresql.Driver");

        private final String value;
        private final String driverClassName;

        Type(String value, String driverClassName) {
            this.value = value;
            this.driverClassName = driverClassName;
        }
    }
}
