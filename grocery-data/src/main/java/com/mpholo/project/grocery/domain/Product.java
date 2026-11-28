package com.mpholo.project.grocery.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mpholo.project.grocery.security.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = {"groceryItem","category","owner"})
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    @NotNull
    private String productName;
    private String productDescription;
    @NotNull
    private double productPrice;

    @OneToMany(mappedBy = "product")
    private Set<GroceryItem> groceryItem = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    // The user who owns this product (per-user catalog).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

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
