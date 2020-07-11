package com.mpholo.project.grocery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private int productId;
    private String productName;
    private String productDescription;
    @JsonProperty("product_url")
    private String productUrl;
}
