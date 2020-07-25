package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.GroceryBasket;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class GroceryBasketListDTO implements Serializable {
    List<GroceryBasket> groceryBasketList;
}
