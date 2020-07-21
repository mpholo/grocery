package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.model.MonthlyGroceryDTO;

public interface MonthlyGroceryService extends CrudService<MonthlyGroceryDTO,Integer> {

    MonthlyGroceryDTO findByPeriod(String period);

}
