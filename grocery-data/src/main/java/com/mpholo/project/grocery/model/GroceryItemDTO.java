package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryItemDTO implements Serializable {

    private int groceryItemId;
    private int quantity;
    private double actualPrice;
    private Product product;
    private MonthlyGrocery monthlyGrocery;
}
