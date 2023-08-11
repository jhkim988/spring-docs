package com.example.webflux.repository;

import com.example.webflux.model.RNumber;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RNumberRepository extends ReactiveCrudRepository<RNumber, Long> {
}
