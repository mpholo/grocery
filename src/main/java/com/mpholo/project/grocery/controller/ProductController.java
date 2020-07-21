package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.service.ProductService;
import com.mpholo.project.grocery.util.ProductAttributeNames;
import com.mpholo.project.grocery.util.ProductMappings;
import com.mpholo.project.grocery.util.ProductViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {

    //==field variables==
    private final ProductService productService;

    //==constructor==
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping(ProductMappings.PRODUCT_LIST)
    public String displayProducts(Model model) {

        List<ProductDTO> productList = productService.findAll();
        log.info("displaying all products: "+productList.size());
        model.addAttribute(ProductAttributeNames.PRODUCT_LIST,productList);
        model.addAttribute(ProductAttributeNames.PRODUCT,new ProductDTO());

        return ProductViewNames.PRODUCTS;
    }

    @PostMapping(ProductMappings.PRODUCT_SAVE)
    public String saveProduct(@ModelAttribute ProductDTO productDTO) {

        productService.save(productDTO);
        log.info("Product from form = {} saved successfully",productDTO);
        return "redirect:/product/"+ProductMappings.PRODUCT_LIST;
    }

}
