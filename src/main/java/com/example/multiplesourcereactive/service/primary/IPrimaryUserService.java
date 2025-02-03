package com.example.multiplesourcereactive.service.primary;

import com.example.multiplesourcereactive.model.primary.PrimaryUser;
import reactor.core.publisher.Mono;

public interface IPrimaryUserService {
    Mono<PrimaryUser> save(PrimaryUser primaryUser);
}
