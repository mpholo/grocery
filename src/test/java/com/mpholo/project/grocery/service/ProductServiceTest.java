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

import static com.mpholo.project.grocery.util.ProductMappings.PRODUCTURL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void createNewProduct() {

        //given
        ProductDTO productDto = new ProductDTO();
        productDto.setProductId(ID);
        productDto.setProductName(NAME);

        Product saveProduct = new Product();
        saveProduct.setProductId(productDto.getProductId());
        saveProduct.setProductName(productDto.getProductName());


        when(productRepository.save(any(Product.class))).thenReturn(saveProduct);

        //when
        ProductDTO saveDTo = productService.save(productDto);

        //then
        assertEquals(productDto.getProductName(),saveDTo.getProductName());
        assertEquals(PRODUCTURL+ID,saveDTo.getProductUrl());
    }

    @Test
    void editProduct() throws  Exception {

        //given
        ProductDTO productDto = new ProductDTO();
        productDto.setProductId(ID);
        productDto.setProductName(NAME);

        Product saveProduct = new Product();
        saveProduct.setProductId(productDto.getProductId());
        saveProduct.setProductName(productDto.getProductName());


        when(productRepository.save(any(Product.class))).thenReturn(saveProduct);

        //when
        ProductDTO productDTO = productService.edit(ID,productDto);

        //then
        assertEquals(productDto.getProductName(),productDTO.getProductName());
        assertEquals(PRODUCTURL+ID,productDTO.getProductUrl());
    }

    @Test
    void patchProduct()  throws Exception {
        //given
        String productUrl = PRODUCTURL+"/"+ID;
        ProductDTO productDto = new ProductDTO();
        productDto.setProductId(ID);
        productDto.setProductName(NAME);
        productDto.setProductUrl(productUrl);
        productDto.setProductDescription("Product Descrition");

        Product saveProduct = new Product();
        saveProduct.setProductId(productDto.getProductId());
        saveProduct.setProductName(productDto.getProductName());


        when(productRepository.save(any(Product.class))).thenReturn(saveProduct);

        //when
        ProductDTO productDTO = productService.patchProduct(ID,productDto);

        //then
        assertEquals(productDto.getProductName(),productDTO.getProductName());
        assertEquals(productUrl,productDto.getProductUrl());
        assertNull(saveProduct.getProductDescription());
    }
}