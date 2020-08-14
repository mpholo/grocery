package com.mpholo.project.grocery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mpholo.project.grocery.domain.GroceryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Serializable {

    private int productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    @JsonProperty("product_url")
    private String productUrl;
    private Set<GroceryItem> groceryBasketSet = new HashSet<>();
}
