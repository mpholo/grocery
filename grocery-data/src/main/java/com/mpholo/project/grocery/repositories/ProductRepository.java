package com.mpholo.project.grocery.repositories;

import com.mpholo.project.grocery.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    Optional<Product> findByProductName(String productName);
}
