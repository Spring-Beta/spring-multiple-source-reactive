package com.example.multiplesourcereactive.service.secondary;

import com.example.multiplesourcereactive.model.secondary.SecondaryCustomer;
import reactor.core.publisher.Mono;

public interface ISecondaryCustomerService {
    Mono<SecondaryCustomer> save(SecondaryCustomer secondaryCustomer);
}
