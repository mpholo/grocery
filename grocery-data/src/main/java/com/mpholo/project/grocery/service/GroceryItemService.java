package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.GroceryItemUpdateDTO;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;

import java.util.List;

public interface GroceryItemService extends CrudService<GroceryItemDTO,Integer> {

   List<GroceryItemDTO> findByMonthlyGrocery(MonthlyGroceryDTO monthlyGroceryDTO);
   List<GroceryItemDTO> findByMonthlyGroceryId(Integer monthlyGroceryId);
   GroceryItemDTO save(GroceryItemUpdateDTO groceryItemUpdateDTO);

   /**
    * Partial update of a grocery item. Any argument left {@code null} is ignored,
    * so callers can change the quantity, price and/or the "added to basket"
    * (purchased) flag independently.
    */
   GroceryItemDTO patch(Integer groceryItemId, Integer quantity, Double actualPrice, Boolean purchased);

}
