package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.service.ProductService;
import com.mpholo.project.grocery.util.ProductAttributeNames;
import com.mpholo.project.grocery.util.ProductMappings;
import com.mpholo.project.grocery.util.ProductViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.mpholo.project.grocery.util.ProductMappings.PRODUCT_REDIRECT_LIST;
import static com.mpholo.project.grocery.util.ProductMappings.PRODUCT_ROOT;

@Slf4j
@Controller
@RequestMapping(PRODUCT_ROOT)
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
    public String saveProduct(@Valid @ModelAttribute("product") ProductDTO productDTO,
                              BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError ->
                    log.debug(objectError.toString())
                    );
            return "redirect:"+PRODUCT_REDIRECT_LIST+"/error";
        }

        productService.save(productDTO);
        log.info("Product from form = {} saved successfully",productDTO);
        return "redirect:"+PRODUCT_REDIRECT_LIST;
    }

    @GetMapping(ProductMappings.PRODUCT_DELETE)
    public String deleteProduct(@RequestParam(name="productId") int productId) {
        log.info("Deleting product with productId {}",productId);
        productService.deleteById(productId);

        return "redirect:"+PRODUCT_REDIRECT_LIST;

    }


}
