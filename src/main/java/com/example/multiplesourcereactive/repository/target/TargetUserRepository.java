package com.example.multiplesourcereactive.repository.target;

import com.example.multiplesourcereactive.model.target.TargetUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetUserRepository extends ReactiveCrudRepository<TargetUser, Long> {
}

