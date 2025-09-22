package common.lib.vtrip.infrastructure.datasource.persistence;

import com.zaxxer.hikari.HikariDataSource;
import common.lib.vtrip.infrastructure.datasource.persistence.datasources.CommonDataSource;
import common.lib.vtrip.infrastructure.datasource.persistence.datasources.MysqlDataSource;
import common.lib.vtrip.infrastructure.datasource.persistence.datasources.PostgresDataSource;
import common.lib.vtrip.infrastructure.datasource.persistence.datasources.RoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass({HikariDataSource.class})
@ConditionalOnMissingBean({DataSource.class})
@EnableTransactionManagement
public class RoutingDataSourceConfiguration {

    @Configuration(
            proxyBeanMethods = false
    )
    @ConditionalOnClass({PostgresDataSource.class})
    @ConditionalOnMissingBean({DataSource.class})
    @ConditionalOnProperty(
            name = {"spring.datasource.source-type"},
            havingValue = "postgresql",
            matchIfMissing = true
    )
    static class Postgres {
        @Bean
        @ConditionalOnProperty(prefix = "spring.datasource.postgresql.read", name = "host")
        @ConfigurationProperties("spring.datasource.postgresql.read")
        public PostgresDataSource readDataSource() {
            return DataSourceBuilder.create().type(PostgresDataSource.class).build();
        }

        @Bean
        @ConfigurationProperties("spring.datasource.postgresql.write")
        public PostgresDataSource writeDataSource() {
            return DataSourceBuilder.create().type(PostgresDataSource.class).build();
        }
    }

    @Configuration(
            proxyBeanMethods = false
    )
    @ConditionalOnClass({MysqlDataSource.class})
    @ConditionalOnMissingBean({DataSource.class})
    @ConditionalOnProperty(
            name = {"spring.datasource.source-type"},
            havingValue = "mysql",
            matchIfMissing = true
    )
    static class Mysql {
        @Bean
        @ConditionalOnProperty(prefix = "spring.datasource.mysql.read", name = "host")
        @ConfigurationProperties("spring.datasource.mysql.read")
        public MysqlDataSource readDataSource() {
            return DataSourceBuilder.create().type(MysqlDataSource.class).build();
        }

        @Bean
        @ConfigurationProperties("spring.datasource.mysql.write")
        public MysqlDataSource writeDataSource() {
            return DataSourceBuilder.create().type(MysqlDataSource.class).build();
        }
    }

    @Bean
    @Primary
    public DataSource routingDataSource(
            @Qualifier("writeDataSource") CommonDataSource writeDataSource,
            @Autowired(required = false) @Qualifier("readDataSource") CommonDataSource readDataSource
    ) {
        return new LazyConnectionDataSourceProxy(RoutingDataSource.fromDataSource(writeDataSource, readDataSource));
    }
}
