package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.ProductDTO;

import java.util.List;

public interface ProductService  extends CrudService<ProductDTO,Integer> {

    ProductDTO findByProductName(String name);
    ProductDTO patchProduct(Integer productId, ProductDTO productDTO);
    List<ProductDTO> availableProducts(List<GroceryItemDTO> GroceryItemListDTO);


}
