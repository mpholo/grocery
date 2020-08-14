package com.mpholo.project.grocery.repositories;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.domain.MonthlyGrocery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroceryItemRepository extends JpaRepository<GroceryItem,Integer> {

      List<GroceryItem> findByMonthlyGrocery(MonthlyGrocery monthlyGrocery);

}
