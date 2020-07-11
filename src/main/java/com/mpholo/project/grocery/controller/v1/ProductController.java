package com.mpholo.project.grocery.controller.v1;

import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.model.ProductListDTO;
import com.mpholo.project.grocery.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.mpholo.project.grocery.util.ProductMappings.PRODUCTURL;

@Controller
@RequestMapping(PRODUCTURL)
public class ProductController {

    private  final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductListDTO> getAllProducts() {
        return new ResponseEntity<ProductListDTO>(
                new ProductListDTO(productService.findAll()),HttpStatus.OK);

    }

    @GetMapping("/{productName}")
    public ResponseEntity<ProductDTO> getProductByMame(@PathVariable String productName) {

        ProductDTO productDTO =productService.findByProductName(productName);
        System.out.println(productName);
        System.out.println("proudct found "+productDTO.getProductName());
        return new ResponseEntity<ProductDTO>(
                productService.findByProductName(productName),HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return new ResponseEntity<ProductDTO>(productService.save(productDTO),HttpStatus.CREATED);
    }


    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> editProduct(@PathVariable Integer productId,@RequestBody ProductDTO productDTO) {
        return  new ResponseEntity<ProductDTO>(
                productService.edit(Integer.valueOf(productId),productDTO),HttpStatus.OK);
    }
}
