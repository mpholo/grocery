package com.mpholo.project.grocery.repositories;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyGroceryRepository extends JpaRepository<MonthlyGrocery,Integer> {

    MonthlyGrocery findByPeriod(String period);

}
