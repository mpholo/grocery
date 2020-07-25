package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryBasketDTO implements Serializable {

    private int groceryBasketId;
    private int quantity;
    private double actualPrice;
    private List<Product> products;
    private MonthlyGrocery monthlyGrocery;
}
