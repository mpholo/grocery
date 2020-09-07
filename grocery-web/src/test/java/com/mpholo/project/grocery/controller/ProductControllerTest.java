package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.mpholo.project.grocery.util.ProductMappings.PRODUCT_REDIRECT_LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 9/7/20
 IDE IntelliJ IDEA
 *******************************************************************/

class ProductControllerTest {


    @Mock
    ProductService productService;

    ProductController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);


        controller = new ProductController(productService);
        mockMvc= MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void testPostNewProductForm() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1);

        when(productService.save(any())).thenReturn(productDTO);

        mockMvc.perform(post("/product/save")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("productId","")
        .param("productName","some String")
        .param("productDescription","some description")
        .param("productPrice","3.98"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"+PRODUCT_REDIRECT_LIST));

    }


    @Test
    void testDeleteProduct() throws  Exception{
        mockMvc.perform(get("/product/delete?productId=1"))
                .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:"+PRODUCT_REDIRECT_LIST));
    }
}