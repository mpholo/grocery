package com.mpholo.project.grocery.repositories;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyGroceryRepository extends JpaRepository<MonthlyGrocery,Integer> {

    MonthlyGrocery findByPeriod(String period);

    // Per-user scoping.
    List<MonthlyGrocery> findByOwner(User owner);
    MonthlyGrocery findByOwnerAndPeriod(User owner, String period);

}
