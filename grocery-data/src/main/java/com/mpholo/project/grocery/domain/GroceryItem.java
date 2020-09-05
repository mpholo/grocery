package com.mpholo.project.grocery.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"product","monthlyGrocery"})
public class GroceryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groceryItemId;
    @NotNull
    private int quantity=1;
    @NotNull
    private double actualPrice;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id")
    @NotNull
    private Product product;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="monthly_grocery_id")
    @NotNull
    private MonthlyGrocery monthlyGrocery;

}
