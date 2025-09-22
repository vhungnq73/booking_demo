# VTRIP JAVA PROJECT TEMPLATE
- JAVA VERSION: 21
- CODING GUIDELINE: https://pmd.github.io/pmd/pmd_rules_java.html

## Datasource Configuration

This template supports both PostgreSQL and MySQL with HikariCP connection pooling.

### Supported Source Types
- `postgresql` - PostgreSQL database
- `mysql` - MySQL database

### Dependencies

#### PostgreSQL
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

#### MySQL
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

### Environment Variables

#### Datasource Configuration
```yaml
spring:
  datasource:
    source-type: {postgresql|mysql}
    postgresql:
      write:
        host: ${DB_HOST}
        port: ${DB_PORT}
        database: ${DB_NAME}
        username: ${DB_USERNAME}
        password: ${DB_PASSWORD}
        # You can config pool setting with Hikari (https://github.com/brettwooldridge/HikariCP)
      read:
        host: ${DB_HOST}
        port: ${DB_PORT}
        database: ${DB_NAME}
        username: ${DB_USERNAME}
        password: ${DB_PASSWORD}
        # You can config pool setting with Hikari (https://github.com/brettwooldridge/HikariCP)
    mysql:
      write:
        host: ${DB_HOST}
        port: ${DB_PORT}
        database: ${DB_NAME}
        username: ${DB_USERNAME}
        password: ${DB_PASSWORD}
        # You can config pool setting with Hikari (https://github.com/brettwooldridge/HikariCP)
      read:
        host: ${DB_HOST}
        port: ${DB_PORT}
        database: ${DB_NAME}
        username: ${DB_USERNAME}
        password: ${DB_PASSWORD}
        # You can config pool setting with Hikari (https://github.com/brettwooldridge/HikariCP)
  jpa:
    open-in-view: false # Required for routing to work properly and performance optimization
```

## Reference Documentation

- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/documentation/)
- [MySQL Connector/J](https://dev.mysql.com/doc/connector-j/en/)
- [HikariCP Connection Pool](https://github.com/brettwooldridge/HikariCP)
- [Spring Boot Database Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-sql)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)


