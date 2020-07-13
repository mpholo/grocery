package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.boostrap.Bootstrap;
import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.mapper.ProductMapper;
import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.repositories.MonthlyGroceryRepository;
import com.mpholo.project.grocery.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
class ProductServiceTestIT {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MonthlyGroceryRepository monthlyGroceryRepository;

    ProductService productService;

    @BeforeEach
    void setUp() throws  Exception {
        System.out.println("Loading Product Data");
        System.out.println(productRepository.findAll().size());

        //setup data for testing
        Bootstrap bootstrap = new Bootstrap(productRepository,monthlyGroceryRepository);
        bootstrap.run();

        productService = new ProductServiceImpl(productRepository, ProductMapper.INSTANCE);

    }

    @Test
    void patchProductUpdateProductName() throws Exception {


        String updatedProductName="updatedName";
        int productId= getProductIDValue();

        Product product = productRepository.findById(productId).get();
        assertNotNull(product);

        String originalProductName = product.getProductName();
        String originalProductDecriptopn = product.getProductDescription();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(productId);
        productDTO.setProductName(updatedProductName);

        productService.patchProduct(productId,productDTO);

        Product updateProduct = productRepository.findById(productId).get();
        assertNotNull(updateProduct);
        assertEquals(updatedProductName,updateProduct.getProductName());
        assertThat(originalProductName,not(equalTo(updateProduct.getProductName())));
        assertThat(originalProductDecriptopn,equalTo(updateProduct.getProductDescription()));

    }

    @Test
    void deleteProductById() throws  Exception {
      Integer id = getProductIDValue();

      int originalRecords = productRepository.findAll().size();
      productRepository.deleteById(id);
      int afterDeleteRecords = productRepository.findAll().size();

      assertThat(originalRecords-1,equalTo(afterDeleteRecords));

    }




    private Integer getProductIDValue() {
        List<Product> productList = productRepository.findAll();
        System.out.println("Product Found: "+productList.size());

        //return first id
        return productList.stream().findFirst().get().getProductId();
    }
}