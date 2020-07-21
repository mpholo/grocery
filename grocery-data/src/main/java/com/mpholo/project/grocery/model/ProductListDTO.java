package com.mpholo.project.grocery.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductListDTO {

    List<ProductDTO> products;

}
