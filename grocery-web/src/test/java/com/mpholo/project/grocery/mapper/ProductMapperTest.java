package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.model.ProductDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {

    ProductMapper productMapper = ProductMapper.INSTANCE;
    public static final int PRODUCT_ID = 1;
    public static final String NAME ="Meat";

    @Test
    void ProductToProductDTO() throws  Exception {

        //given
        Product product = new Product();
        product.setProductId(PRODUCT_ID);

        product.setProductName(NAME);

        //when
        ProductDTO productDTO = productMapper.ProductToProductDTO(product);

        //then
        assertEquals(PRODUCT_ID,productDTO.getProductId());
        assertEquals(NAME,productDTO.getProductName());

    }
}