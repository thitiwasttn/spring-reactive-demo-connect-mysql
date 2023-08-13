package com.thitiwas.reactive.democonnectapi.repository;

import com.thitiwas.reactive.democonnectapi.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
}
