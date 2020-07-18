package com.mpholo.project.grocery.controller.v2;

import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.service.ProductService;
import com.mpholo.project.grocery.util.ProductAttributeNames;
import com.mpholo.project.grocery.util.ProductMappings;
import com.mpholo.project.grocery.util.ProductViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController2 {

    private final ProductService productService;

    public ProductController2(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(ProductMappings.PRODUCT_LIST)
    public String displayProducts(Model model) {

        List<ProductDTO> productList = productService.findAll();
        log.info("displaying all products: "+productList.size());
        model.addAttribute(ProductAttributeNames.PRODUCT_LIST,productList);

        return ProductViewNames.PRODUCTS;
    }
}
