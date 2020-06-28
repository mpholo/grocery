package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.mapper.ProductMapper;
import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private static final int ID=1;
    private static final String NAME="milk";
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        productService = new ProductServiceImpl(productRepository,ProductMapper.INSTANCE);

    }

    @Test
    void getAllProducts() throws  Exception {
        //given
        List<Product> products = Arrays.asList(new Product(),new Product(),new Product());
         when(productRepository.findAll()).thenReturn(products);

        //when
         List<ProductDTO> productDTOS = productService.findAll();

        //then
        assertEquals(3,productDTOS.size());
    }


    @Test
    void getProductByName() {
        //given
        Product product = new Product();
        product.setProductId(ID);
        product.setProductName(NAME);
        when(productRepository.findByProductName(anyString())).thenReturn(product);

        //when
        ProductDTO productDTO = productService.findByProductName(NAME);


        //then
        assertEquals(ID,productDTO.getProductId());
        assertEquals(NAME,productDTO.getProductName());
    }
}