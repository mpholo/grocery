package com.mpholo.project.grocery.api.v1;

import com.mpholo.project.grocery.service.GroceryBasketService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("GroceryBasketControllerApi")
@RequestMapping("/v1/grocerybasket")
public class GroceryBasketController {

    private final GroceryBasketService groceryBasketService;

    public GroceryBasketController(GroceryBasketService groceryBasketService) {

        this.groceryBasketService = groceryBasketService;
    }



}
