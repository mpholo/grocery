package com.mpholo.project.grocery.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductListDTO implements Serializable  {

    List<ProductDTO> products;

}
