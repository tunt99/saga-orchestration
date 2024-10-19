package com.saga.orchestration.repositories;

import com.saga.orchestration.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
