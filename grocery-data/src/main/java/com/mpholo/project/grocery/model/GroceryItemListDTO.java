package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.GroceryItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class GroceryItemListDTO implements Serializable {
    List<GroceryItem> groceryBasketList;
}
