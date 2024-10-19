package com.saga.orchestration.repositories;

import com.saga.orchestration.entities.CardDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDetailRepository extends JpaRepository<CardDetail, String> {
}
