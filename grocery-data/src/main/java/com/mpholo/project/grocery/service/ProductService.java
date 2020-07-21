package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.model.ProductDTO;

public interface ProductService  extends CrudService<ProductDTO,Integer> {

    ProductDTO findByProductName(String name);
    ProductDTO patchProduct(Integer productId, ProductDTO productDTO);

}
