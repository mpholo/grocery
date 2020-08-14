package com.mpholo.project.grocery.api.v1;

import com.mpholo.project.grocery.service.GroceryItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("GroceryBasketControllerApi")
@RequestMapping("/v1/grocerybasket")
public class GroceryBasketController {

    private final GroceryItemService groceryBasketService;

    public GroceryBasketController(GroceryItemService groceryBasketService) {

        this.groceryBasketService = groceryBasketService;
    }



}
