package com.mpholo.project.grocery.repositories;

import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    Optional<Product> findByProductName(String productName);

    // Per-user catalog scoping.
    List<Product> findByOwner(User owner);
    Optional<Product> findByOwnerAndProductName(User owner, String productName);
}
