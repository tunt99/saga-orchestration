package com.saga.orchestration.repositories;

import com.saga.orchestration.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,String> {
}
