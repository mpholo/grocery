package com.mpholo.project.grocery.repositories;

import com.mpholo.project.grocery.domain.GroceryBasket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroceryBasketRepository extends JpaRepository<GroceryBasket,Integer> {



}
