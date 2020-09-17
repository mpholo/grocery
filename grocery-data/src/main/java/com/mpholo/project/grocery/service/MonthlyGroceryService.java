package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.model.MonthlyGroceryDTO;

import java.util.List;
import java.util.Optional;

public interface MonthlyGroceryService extends CrudService<MonthlyGroceryDTO,Integer> {

    MonthlyGroceryDTO findByPeriod(String period);
    List<MonthlyGroceryDTO> findByYear(Integer year);
    Optional<MonthlyGroceryDTO> copy(Integer monthlyGroceryId);

}
