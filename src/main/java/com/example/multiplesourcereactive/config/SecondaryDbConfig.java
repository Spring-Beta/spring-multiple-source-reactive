package com.example.multiplesourcereactive.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = "com.example.multiplesourcereactive.repository.secondary",
        entityOperationsRef = "secondaryTemplate"
)
@Slf4j
public class SecondaryDbConfig {

    @Value("${spring.r2dbc.datasources.secondary.url}")
    private String url;
    @Value("${spring.r2dbc.datasources.secondary.username}")
    private String username;
    @Value("${spring.r2dbc.datasources.secondary.password}")
    private String password;

    @Bean("secondaryConnectionFactory")
    public ConnectionFactory secondaryConnectionFactory() {
        log.info("✅ Connected to Secondary Database at {}", "r2dbc:postgresql://localhost:5402/secondary_db");
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .from(ConnectionFactoryOptions.parse(url))
                        .option(ConnectionFactoryOptions.USER, username)
                        .option(ConnectionFactoryOptions.PASSWORD, password)
                        .build()
        );
    }

    @Bean("secondaryTransactionManager")
    public R2dbcTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryConnectionFactory") ConnectionFactory connectionFactory) {
        log.info("🔄 Initializing Secondary Transaction Manager...");
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean("secondaryTransactionalOperator")
    public TransactionalOperator secondaryTransactionalOperator(
            @Qualifier("secondaryTransactionManager") R2dbcTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }


    @Bean("secondaryDatabaseClient")
    public DatabaseClient secondaryDatabaseClient(
            @Qualifier("secondaryConnectionFactory") ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }

    @Bean("secondaryTemplate")
    public R2dbcEntityTemplate secondaryTemplate(
            @Qualifier("secondaryConnectionFactory") ConnectionFactory connectionFactory) {
        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(
                PostgresDialect.INSTANCE);
        log.info("🔄 Initializing Secondary EntityManagerFactory...");
        return new R2dbcEntityTemplate(DatabaseClient.create(connectionFactory), strategy);
    }
}
