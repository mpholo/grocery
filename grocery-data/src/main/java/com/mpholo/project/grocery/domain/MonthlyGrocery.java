package com.mpholo.project.grocery.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Entity
@Setter
@Getter
@EqualsAndHashCode(exclude = {"groceryItems"})
public class MonthlyGrocery  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int monthlyGroceryId;
    @NotNull
    private double budgetAmount;
    @NotNull
    private String period;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate startDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private  LocalDate endDate;

    @OneToMany(cascade={CascadeType.ALL},mappedBy = "monthlyGrocery")
    private Set<GroceryItem> groceryItems = new HashSet<>();

    public void addItems(Set<GroceryItem> groceryItems) {
        for(GroceryItem item:groceryItems) {

            GroceryItem newItem= GroceryItem.builder().quantity(item.getQuantity())
                    .actualPrice(item.getActualPrice())
                    .product(item.getProduct())
                    .monthlyGrocery(this).build();


            if(!this.groceryItems.add(newItem)) {
                log.warn("This item {} of grocery {} wont be added. It is a duplicate",newItem.getProduct().getProductName(),this.getPeriod());
            }
        }
    }

}
