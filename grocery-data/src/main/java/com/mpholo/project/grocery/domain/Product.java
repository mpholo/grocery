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
    private Integer productId;
    @NotNull
    private String productName;
    private String productDescription;
    @NotNull
    private double productPrice;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private Set<GroceryItem> groceryItem = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", groceryItem=" + groceryItem +
                ", category=" + category +
                '}';
    }
}
