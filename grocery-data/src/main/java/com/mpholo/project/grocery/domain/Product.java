package com.mpholo.project.grocery.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = {"groceryItem","category"})
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    @NotNull
    private String productName;
    private String productDescription;
    @NotNull
    private double productPrice;

    @OneToMany
    @JoinColumn(name="product_id")
    private Set<GroceryItem> groceryItem = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

}
