package com.mpholo.project.grocery.repositories;

import com.mpholo.project.grocery.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    Product findByProductName(String productName);
}
