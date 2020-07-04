package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.GroceryBasket;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroceryBasketListDTO {
    List<GroceryBasket> groceryBasketList;
}
