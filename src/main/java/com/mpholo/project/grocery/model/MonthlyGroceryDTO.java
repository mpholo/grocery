package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.GroceryBasket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyGroceryDTO {


    private int groceryId;
    private double budgetAmount;
    private String period;
    private LocalDate startDate;
    private  LocalDate endDate;
    private GroceryBasket groceryBasket;


}
