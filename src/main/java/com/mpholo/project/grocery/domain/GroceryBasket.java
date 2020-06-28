package com.mpholo.project.grocery.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class GroceryBasket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int monthlyGroceryId;
    private int quantity;
    private double actual_price;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product",fetch = FetchType.LAZY)
    private List<Product> product;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "monthlygrocery",fetch = FetchType.LAZY)
    private List<MonthlyGrocery> grocery;

}
