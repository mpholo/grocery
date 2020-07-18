package com.mpholo.project.grocery.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    @OneToMany
    @JoinColumn(name="product_id")
    private Set<GroceryBasket> groceryBasket;




}
