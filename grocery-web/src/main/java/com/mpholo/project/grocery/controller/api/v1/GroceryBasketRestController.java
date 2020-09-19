package com.mpholo.project.grocery.controller.api.v1;

import com.mpholo.project.grocery.service.GroceryItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("GroceryBasketControllerApi")
@RequestMapping("/v1/grocerybasket")
public class GroceryBasketRestController {

    private final GroceryItemService groceryBasketService;

    public GroceryBasketRestController(GroceryItemService groceryBasketService) {

        this.groceryBasketService = groceryBasketService;
    }



}
