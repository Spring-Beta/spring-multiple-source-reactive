package com.example.multiplesourcereactive.service.secondary.impl;

import com.example.multiplesourcereactive.model.secondary.SecondaryCustomer;
import com.example.multiplesourcereactive.repository.secondary.SecondaryCustomerRepository;
import com.example.multiplesourcereactive.service.secondary.ISecondaryCustomerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SecondaryCustomerServiceService implements ISecondaryCustomerService {
    private final SecondaryCustomerRepository secondaryCustomerRepository;

    public SecondaryCustomerServiceService(SecondaryCustomerRepository secondaryCustomerRepository) {
        this.secondaryCustomerRepository = secondaryCustomerRepository;
    }

    @Override
    public Mono<SecondaryCustomer> save(SecondaryCustomer secondaryCustomer) {
        return secondaryCustomerRepository.save(secondaryCustomer);
    }
}
