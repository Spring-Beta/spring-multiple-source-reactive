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
        basePackages = "com.example.multiplesourcereactive.repository.target",
        entityOperationsRef = "targetTemplate"
)
@Slf4j
public class TargetDbConfig {
    
    @Value("${spring.r2dbc.datasources.target.url}")
    private String url;
    @Value("${spring.r2dbc.datasources.target.username}")
    private String username;
    @Value("${spring.r2dbc.datasources.target.password}")
    private String password;

    @Bean("targetConnectionFactory")
    public ConnectionFactory targetConnectionFactory() {
        log.info("✅ Connected to Target Database at {}", "r2dbc:postgresql://localhost:5403/target_db");
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .from(ConnectionFactoryOptions.parse(url))
                        .option(ConnectionFactoryOptions.USER, username)
                        .option(ConnectionFactoryOptions.PASSWORD, password)
                        .build()
        );
    }

    @Bean("targetTransactionManager")
    public R2dbcTransactionManager targetTransactionManager(
            @Qualifier("targetConnectionFactory") ConnectionFactory connectionFactory) {
        log.info("🔄 Initializing Target Transaction Manager...");
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean("targetTransactionalOperator")
    public TransactionalOperator targetTransactionalOperator(
            @Qualifier("targetTransactionManager") R2dbcTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }


    @Bean("targetDatabaseClient")
    public DatabaseClient targetDatabaseClient(
            @Qualifier("targetConnectionFactory") ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }

    @Bean("targetTemplate")
    public R2dbcEntityTemplate targetTemplate(
            @Qualifier("targetConnectionFactory") ConnectionFactory connectionFactory) {
        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(
                PostgresDialect.INSTANCE);
        log.info("🔄 Initializing Target EntityManagerFactory...");
        return new R2dbcEntityTemplate(DatabaseClient.create(connectionFactory), strategy);
    }
    
}
