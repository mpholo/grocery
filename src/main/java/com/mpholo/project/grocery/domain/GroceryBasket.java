package com.mpholo.project.grocery.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"products"})
public class GroceryBasket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groceryBasketId;
    private int quantity;
    private double actualPrice;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "groceryBasket",fetch = FetchType.LAZY)
    private List<Product> products;
    @ManyToOne(fetch=FetchType.EAGER)
    private MonthlyGrocery monthlyGrocery;

}
