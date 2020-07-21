package com.mpholo.project.grocery.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"groceryBasket"})
public class MonthlyGrocery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int monthlyGroceryId;
    private double budgetAmount;
    private String period;
    private LocalDate startDate;
    private  LocalDate endDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="monthly_grocery_id")
    private Set<GroceryBasket> groceryBasket = new HashSet<>();

}
