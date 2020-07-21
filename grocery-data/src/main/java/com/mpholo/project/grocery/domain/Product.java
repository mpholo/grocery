package com.mpholo.project.grocery.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = {"groceryBasket"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    @OneToMany
    @JoinColumn(name="product_id")
    private Set<GroceryBasket> groceryBasket = new HashSet<>();




}
