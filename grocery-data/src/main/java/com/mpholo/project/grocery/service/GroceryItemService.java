package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;

import java.util.List;

public interface GroceryItemService extends CrudService<GroceryItemDTO,Integer> {

   List<GroceryItemDTO> findByMonthlyGrocery(MonthlyGroceryDTO monthlyGroceryDTO);

}
