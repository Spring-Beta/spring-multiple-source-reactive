package com.example.multiplesourcereactive.repository.secondary;

import com.example.multiplesourcereactive.model.secondary.SecondaryCustomer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondaryCustomerRepository extends ReactiveCrudRepository<SecondaryCustomer, Long> {
}

