package com.mpholo.project.grocery.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
public class MonthlyGrocery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groceryId;
    private double budgetAmount;
    private String period;
    private LocalDate startDate;
    private  LocalDate endDate;


    private GroceryBasket groceryBasket;

}
