package com.mpholo.project.grocery.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"product","monthlyGrocery"})
public class GroceryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groceryItemId;
    private int quantity;
    private double actualPrice;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product product;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="monthly_grocery_id")
    private MonthlyGrocery monthlyGrocery;

}
