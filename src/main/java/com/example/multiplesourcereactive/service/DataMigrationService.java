package com.example.multiplesourcereactive.service;

import com.example.multiplesourcereactive.model.primary.PrimaryUser;
import com.example.multiplesourcereactive.model.secondary.SecondaryCustomer;
import com.example.multiplesourcereactive.model.target.TargetUser;
import com.example.multiplesourcereactive.repository.primary.PrimaryUserRepository;
import com.example.multiplesourcereactive.repository.secondary.SecondaryCustomerRepository;
import com.example.multiplesourcereactive.repository.target.TargetUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@Slf4j
public class DataMigrationService {

    private final PrimaryUserRepository primaryUserRepository;
    private final SecondaryCustomerRepository secondaryCustomerRepository;
    private final TargetUserRepository targetUserRepository;

    private final TransactionalOperator primaryTransactionalOperator;
    private final TransactionalOperator secondaryTransactionalOperator;
    private final TransactionalOperator targetTransactionalOperator;

    public DataMigrationService(
            PrimaryUserRepository primaryUserRepository,
            SecondaryCustomerRepository secondaryCustomerRepository,
            TargetUserRepository targetUserRepository,
            @Qualifier("primaryTransactionalOperator") TransactionalOperator primaryTransactionalOperator,
            @Qualifier("secondaryTransactionalOperator") TransactionalOperator secondaryTransactionalOperator,
            @Qualifier("targetTransactionalOperator") TransactionalOperator targetTransactionalOperator) {
        this.primaryUserRepository = primaryUserRepository;
        this.secondaryCustomerRepository = secondaryCustomerRepository;
        this.targetUserRepository = targetUserRepository;
        this.primaryTransactionalOperator = primaryTransactionalOperator;
        this.secondaryTransactionalOperator = secondaryTransactionalOperator;
        this.targetTransactionalOperator = targetTransactionalOperator;
    }

    private Flux<PrimaryUser> getAllUsers() {
        log.info("✅ Fetching data from primary database...");
        return primaryUserRepository.findAll();
    }

    private Flux<SecondaryCustomer> getAllCustomers() {
        log.info("✅ Fetching data from secondary database...");
        return secondaryCustomerRepository.findAll();
    }

    public Flux<TargetUser> migrateData() {
        log.info("🚀 Starting Data Migration...");

        return primaryTransactionalOperator.transactional( // 🔹 Start Primary DB Transaction
                getAllUsers()
                        .collectList() // ✅ Collect data before transaction boundary
                        .flatMapMany(primaryUsers ->
                                secondaryTransactionalOperator.transactional( // 🔹 Start Secondary DB Transaction
                                        getAllCustomers()
                                                .collectList()
                                                .flatMapMany(secondaryCustomers -> {
                                                    if (primaryUsers.isEmpty() || secondaryCustomers.isEmpty()) {
                                                        log.warn("⚠ No data found in primary or secondary databases.");
                                                        return Flux.empty();
                                                    }

                                                    return Flux.zip(Flux.fromIterable(primaryUsers), Flux.fromIterable(secondaryCustomers))
                                                            .map(tuple -> {
                                                                PrimaryUser user = tuple.getT1();
                                                                TargetUser targetUser = getTargetUser(tuple, user);

                                                                log.info("✅ Transformed Target Data: {}", targetUser);
                                                                return targetUser;
                                                            });
                                                })
                                )
                        )
        ).collectList()
                .flatMapMany(targetUsers -> targetTransactionalOperator.transactional( // 🔹 Start Target DB Transaction
                 Flux.fromIterable(targetUsers)
                        .flatMap(targetUserRepository::save) // Save transformed users
                        .doOnError(error -> log.error("❌ Error saving data to Target DB: {}", error.getMessage()))
        )); // ✅ Return a Mono<Void> to indicate successful completion
    }

    private static TargetUser getTargetUser(Tuple2<PrimaryUser, SecondaryCustomer> tuple, PrimaryUser user) {
        SecondaryCustomer customer = tuple.getT2();

        TargetUser targetUser = new TargetUser();
        targetUser.setName(user.getName());
        targetUser.setEmail(user.getEmail());
        targetUser.setFullName(customer.getFullName());
        targetUser.setContact(customer.getContact());
        return targetUser;
    }
}
