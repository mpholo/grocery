package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.GroceryItem;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"groceryItems"})
public class MonthlyGroceryDTO implements Serializable  {

    private Integer monthlyGroceryId;
    private double budgetAmount;
    private String period;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private  LocalDate endDate;
    private Set<GroceryItem> groceryItems= new HashSet<>();


}
