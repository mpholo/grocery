package com.mpholo.project.grocery.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"monthlyGrocery"})
public class GroceryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groceryItemId;
    @NotNull
    private int quantity=1;
    @NotNull
    private double actualPrice;
    private boolean purchased;


    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product product;

    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="monthly_grocery_id")
    private MonthlyGrocery monthlyGrocery;

    @Override
    public String toString() {
        return "GroceryItem{" +
                "groceryItemId=" + groceryItemId +
                ", quantity=" + quantity +
                ", actualPrice=" + actualPrice +
                ", product=" + product +
                ", monthlyGrocery=" + monthlyGrocery +
                '}';
    }
}
