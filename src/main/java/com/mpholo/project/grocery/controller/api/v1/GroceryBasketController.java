package com.mpholo.project.grocery.controller.api.v1;

import com.mpholo.project.grocery.service.GroceryBasketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/grocerybasket")
public class GroceryBasketController {

    private final GroceryBasketService groceryBasketService;

    public GroceryBasketController(GroceryBasketService groceryBasketService) {

        this.groceryBasketService = groceryBasketService;
    }



}
