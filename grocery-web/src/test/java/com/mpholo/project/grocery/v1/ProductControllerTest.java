package com.mpholo.project.grocery.v1;

import com.mpholo.project.grocery.controller.api.v1.RestResponseEntityExceptionHandler;
import com.mpholo.project.grocery.controller.ProductController;
import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.service.ProductService;
import com.mpholo.project.grocery.service.ResourceNotFoundException;
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

import static com.mpholo.project.grocery.util.ProductMappings.PRODUCTURL;
import static com.mpholo.project.grocery.v1.AbstractRestController.asJsonString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
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

        mockMvc.perform(get(PRODUCTURL)
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


        mockMvc.perform(get(PRODUCTURL+"/milk")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", equalTo(NAME)));
    }

    @Test
    void testFindByProductNameNotFound() throws  Exception {

        when(productService.findByProductName(anyString())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(PRODUCTURL+"/unknown")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void createNewProduct() throws  Exception {

        //given
        ProductDTO product1 = new ProductDTO();
        product1.setProductId(1);
        product1.setProductName(NAME);

        ProductDTO returnDTO = new ProductDTO();
        returnDTO.setProductId(product1.getProductId());
        returnDTO.setProductName(product1.getProductName());
        returnDTO.setProductUrl(PRODUCTURL+1);

        when(productService.save(product1)).thenReturn(returnDTO);

        //FOR DEBUGGIN
//       String response= mockMvc.perform(post(PRODUCTURL)
//       .contentType(MediaType.APPLICATION_JSON)
//       .content(asJsonString(product1)))
//               .andReturn().getResponse().getContentAsString();

//        System.out.println(response);

        mockMvc.perform(post(PRODUCTURL)
                .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(product1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName",equalTo(NAME)))
                .andExpect(jsonPath("$.product_url",equalTo(PRODUCTURL+product1.getProductId())));
    }


    @Test
    void editProductController()  throws Exception{

        //given
        ProductDTO product1 = new ProductDTO();
        product1.setProductId(1);
        product1.setProductName(NAME);

        String productUrl= PRODUCTURL+"/1";
        ProductDTO returnDTO = new ProductDTO();
        returnDTO.setProductId(product1.getProductId());
        returnDTO.setProductName(product1.getProductName());
        returnDTO.setProductUrl(productUrl);

        when(productService.edit(anyInt(),any(ProductDTO.class))).thenReturn(returnDTO);

        //FOR DEBUGGIN
//       String response= mockMvc.perform(put(productUrl)
//       .contentType(MediaType.APPLICATION_JSON)
//       .content(asJsonString(product1)))
//               .andReturn().getResponse().getContentAsString();
//
//        System.out.println(response);


        //when/then

        mockMvc.perform(put(productUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName",equalTo(NAME)))
                .andExpect(jsonPath("$.product_url",equalTo(productUrl)));

    }

    @Test
    void patchProductController() throws  Exception {
        //given
        ProductDTO product1 = new ProductDTO();
        product1.setProductId(1);
        product1.setProductName(NAME);
        product1.setProductDescription("Old description");

        String productUrl= PRODUCTURL+"/1";
        ProductDTO returnDTO = new ProductDTO();
        returnDTO.setProductId(product1.getProductId());
        returnDTO.setProductName(product1.getProductName());
        returnDTO.setProductUrl(productUrl);

        when(productService.patchProduct(anyInt(), any(ProductDTO.class))).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(patch(productUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName",equalTo(NAME)))
                .andExpect(jsonPath("$.productDescription",nullValue()))
                .andExpect(jsonPath("$.product_url",equalTo(productUrl)));


    }

    @Test
    void testDeleteCustomer() throws  Exception {
        mockMvc.perform(delete(PRODUCTURL+"/1")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService,times(1)).deleteById(anyInt());
    }
}