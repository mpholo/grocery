package com.mpholo.project.grocery.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@EqualsAndHashCode(exclude = {"groceryItem"})
public class MonthlyGrocery  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int monthlyGroceryId;
    private double budgetAmount;
    private String period;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private  LocalDate endDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="monthly_grocery_id")
    private Set<GroceryItem> groceryItem = new HashSet<>();

}
