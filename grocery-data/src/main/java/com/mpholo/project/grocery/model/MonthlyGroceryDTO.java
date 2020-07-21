package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.GroceryBasket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyGroceryDTO {

    private int monthlyGroceryId;
    private double budgetAmount;
    private String period;
    private LocalDate startDate;
    private  LocalDate endDate;
    private Set<GroceryBasket> groceryBasket = new HashSet<>();


}
