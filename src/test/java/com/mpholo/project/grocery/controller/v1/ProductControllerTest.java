package com.mpholo.project.grocery.controller.v1;

import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private static final String NAME ="milk";

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService productService;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testListProducts()  throws Exception {

        //given
        ProductDTO salt = new ProductDTO();
        salt.setProductId(1);
        salt.setProductName("Salt");

        ProductDTO milk = new ProductDTO();
        milk.setProductName("Milk");
        milk.setProductId(2);

        ProductDTO cheese = new ProductDTO();
        cheese.setProductId(3);
        cheese.setProductName("Cheese");

        ProductDTO coffee = new ProductDTO();
        coffee.setProductId(4);
        coffee.setProductName("Coffee");

        ProductDTO rice = new ProductDTO();
        rice.setProductId(5);
        rice.setProductName("Rice");

        List<ProductDTO> productList = Arrays.asList(salt,milk,cheese,coffee,rice);

        when(productService.findAll()).thenReturn(productList);

        mockMvc.perform(get("/api/v1/products/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(5)));


    }

    @Test
    public void testFindByProductName() throws Exception {
        ProductDTO product1 = new ProductDTO();
        product1.setProductId(1);
        product1.setProductName(NAME);

        when(productService.findByProductName(anyString())).thenReturn(product1);


        mockMvc.perform(get("/api/v1/products/milk")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", equalTo(NAME)));
    }
}