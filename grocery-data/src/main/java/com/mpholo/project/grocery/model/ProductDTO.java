package com.mpholo.project.grocery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mpholo.project.grocery.domain.GroceryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"groceryItems"})
public class ProductDTO implements Serializable {

    private Integer productId;

    @NotBlank
    @Size(min = 3, max = 50)
    private String productName;
    private String productDescription;
    private double productPrice;
    @JsonProperty("product_url")
    @URL
    private String productUrl;
    private Set<GroceryItem> groceryItems = new HashSet<>();
}
