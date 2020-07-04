package com.mpholo.project.grocery.repositories;

import com.mpholo.project.grocery.domain.GroceryBasket;

import java.util.List;

public interface GroceryBasketRepository extends GenericRepository<GroceryBasket,Integer> {


    List<GroceryBasket> findByPeriod(String period);
}
