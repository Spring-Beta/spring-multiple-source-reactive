package com.example.multiplesourcereactive.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(
        basePackages = "com.example.multiplesourcereactive.repository.primary",
        entityOperationsRef = "primaryTemplate"
)
@Slf4j
public class PrimaryDbConfig {

    @Value("${spring.r2dbc.datasources.primary.url}")
    private String url;
    @Value("${spring.r2dbc.datasources.primary.username}")
    private String username;
    @Value("${spring.r2dbc.datasources.primary.password}")
    private String password;

    @Bean("primaryConnectionFactory")
    public ConnectionFactory primaryConnectionFactory() {
        log.info("✅ Connected to Primary Database at {}", "r2dbc:postgresql://localhost:5401/primary_db");
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .from(ConnectionFactoryOptions.parse(url))
                        .option(ConnectionFactoryOptions.USER, username)
                        .option(ConnectionFactoryOptions.PASSWORD, password)
                        .build()
        );
    }

    @Bean("primaryTransactionManager")
    @Primary
    public R2dbcTransactionManager primaryTransactionManager(
            @Qualifier("primaryConnectionFactory") ConnectionFactory connectionFactory) {
        log.info("🔄 Initializing Primary Transaction Manager...");
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean("primaryTransactionalOperator")
    public TransactionalOperator primaryTransactionalOperator(
            @Qualifier("primaryTransactionManager") R2dbcTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

    @Bean("primaryDatabaseClient")
    public DatabaseClient primaryDatabaseClient(
            @Qualifier("primaryConnectionFactory") ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }

    @Bean("primaryTemplate")
    public R2dbcEntityTemplate primaryTemplate(
            @Qualifier("primaryConnectionFactory") ConnectionFactory connectionFactory) {
        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(
                PostgresDialect.INSTANCE);
        log.info("🔄 Initializing Primary EntityManagerFactory...");
        return new R2dbcEntityTemplate(DatabaseClient.create(connectionFactory), strategy);
    }
}
