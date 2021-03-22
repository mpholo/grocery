package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.domain.Product;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"product","monthlyGrocery"})
public class GroceryItemDTO implements Serializable {

    private int groceryItemId;
    private int quantity=1;
    private double actualPrice;
    private Product product;
    private MonthlyGrocery monthlyGrocery;
    private  double totalPrice;
    private boolean purchased;

    private double calculatedTotalPrice() {
        return actualPrice*quantity;
    }

    @Override
    public String toString() {
        return  product.getProductName() +","+
                quantity +","+
                actualPrice+","
                +calculatedTotalPrice();
    }
}
