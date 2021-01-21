package com.mpholo.project.grocery.controller.api.v1;

import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.model.ProductListDTO;
import com.mpholo.project.grocery.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.mpholo.project.grocery.util.ProductMappings.PRODUCTURL;

@Api("This is a list of products controller")
@RestController
@RequestMapping(PRODUCTURL)
public class ProductRestController {

    private  final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "This will get list of products")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductListDTO getAllProducts() {
        return  new ProductListDTO(productService.findAll());

    }

    @ApiOperation(value = "This retrieve a product by name")
    @GetMapping("/{productName}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProductByMame(@PathVariable String productName) {

        ProductDTO productDTO =productService.findByProductName(productName);
        System.out.println(productName);
        System.out.println("proudct found "+productDTO.getProductName());
        return  productService.findByProductName(productName);

    }

    @ApiOperation(value = "This will create a new product")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }


    @ApiOperation(value = "This update a product")
    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO editProduct(@PathVariable Integer productId,@RequestBody ProductDTO productDTO) {
        return  productService.edit(Integer.valueOf(productId),productDTO);
    }

    @ApiOperation(value = "This update a product changes fields only")
    @PatchMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO patchProduct(@PathVariable Integer productId,@RequestBody ProductDTO productDTO ) {
        return  productService.patchProduct(Integer.valueOf(productId),productDTO);
    }

    @ApiOperation(value = "This delete a product product")
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void  deleteProduct(@PathVariable Integer productId) {
        productService.deleteById(Integer.valueOf(productId));

    }
}
