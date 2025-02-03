package com.example.multiplesourcereactive.repository.primary;

import com.example.multiplesourcereactive.model.primary.PrimaryUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimaryUserRepository extends ReactiveCrudRepository<PrimaryUser, Long> {
}

