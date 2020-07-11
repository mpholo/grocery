package com.mpholo.project.grocery.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String productName;
    private String productDescription;
    @ManyToOne
    private GroceryBasket groceryBasket;




}
