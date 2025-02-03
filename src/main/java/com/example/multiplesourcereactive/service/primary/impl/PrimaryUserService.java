package com.example.multiplesourcereactive.service.primary.impl;

import com.example.multiplesourcereactive.model.primary.PrimaryUser;
import com.example.multiplesourcereactive.repository.primary.PrimaryUserRepository;
import com.example.multiplesourcereactive.service.primary.IPrimaryUserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PrimaryUserService implements IPrimaryUserService {
    private final PrimaryUserRepository primaryUserRepository;

    public PrimaryUserService(PrimaryUserRepository primaryUserRepository) {
        this.primaryUserRepository = primaryUserRepository;
    }


    @Override
    public Mono<PrimaryUser> save(PrimaryUser primaryUser) {
        return primaryUserRepository.save(primaryUser);
    }
}
