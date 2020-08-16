package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.mapper.MonthlyGroceryMapper;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.GroceryItemUpdateDTO;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.service.GroceryItemService;
import com.mpholo.project.grocery.service.MonthlyGroceryService;
import com.mpholo.project.grocery.service.ProductService;
import com.mpholo.project.grocery.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mpholo.project.grocery.util.GroceryItemMappings.GROCERY_ITEM_REDIRECT_LIST;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/10
 IDE IntelliJ IDEA
 *******************************************************************/

@Slf4j
@Controller
@RequestMapping(GroceryItemMappings.GROCERY_ITEM_ROOT)
public class GroceryItemController {

    private final GroceryItemService groceryItemService;
    private final MonthlyGroceryService monthlyGroceryService;
    private final ProductService productService;

    public GroceryItemController(GroceryItemService groceryItemService, MonthlyGroceryService monthlyGroceryService, ProductService productService) {
        this.groceryItemService = groceryItemService;
        this.monthlyGroceryService = monthlyGroceryService;
        this.productService = productService;
    }

    @GetMapping(GroceryItemMappings.GROCERY_ITEM_LIST)
    public String displayGroceryItems(Model model, @RequestParam(name="monthlyGroceryId") int monthlyGroceryId ) {

        MonthlyGroceryDTO monthlyGroceryDTO = monthlyGroceryService.findById(monthlyGroceryId).get();
        List<GroceryItemDTO> groceryItemList = groceryItemService.findByMonthlyGrocery(monthlyGroceryDTO);
        log.info("Total number of monthly grocery items for {} {}",monthlyGroceryDTO.getPeriod(),groceryItemList.size());

        model.addAttribute(MonthlyGroceryAttributeNames.MONTHLY_GROCERY,monthlyGroceryDTO);
        model.addAttribute(GroceryItemAttributeNames.GROCERY_ITEM_LIST,groceryItemList);

        MonthlyGrocery monthlyGrocery = MonthlyGroceryMapper.INSTACE.monthlyGroceryDTOToMonthlyGrocery(monthlyGroceryDTO);

        GroceryItemUpdateDTO item=new GroceryItemUpdateDTO();
        model.addAttribute(GroceryItemAttributeNames.GROCERY_ITEM,item);

        List<ProductDTO> productList = productService.findAll();
        log.info("displaying all products: "+productList.size());
        model.addAttribute(ProductAttributeNames.PRODUCT_LIST,productList);

        return GroceryItemViewNames.GROCERY_ITEMS;
    }

    @GetMapping(GroceryItemMappings.GROCERY_ITEM_DELETE)
    public String deleteGroceryItem(@RequestParam(name="groceryItemId") int groceryItemId,
                                    @RequestParam(name="monthGroceryId") int monthlyGroceryId) {
        groceryItemService.deleteById(groceryItemId);
        log.info("deleted grocery item {}",groceryItemId);
        return "redirect:"+GROCERY_ITEM_REDIRECT_LIST+monthlyGroceryId;
    }

    @PostMapping(GroceryItemMappings.GROCERY_ITEM_SAVE)
    public String saveGroceryItem(@ModelAttribute GroceryItemUpdateDTO groceryItemUpdateDTO) {

        GroceryItemDTO savedGroceryItemDTO = groceryItemService.save(groceryItemUpdateDTO);
        log.info("grocery item {} saved successfully",savedGroceryItemDTO.getProduct().getProductName());
        return "redirect:"+GROCERY_ITEM_REDIRECT_LIST+savedGroceryItemDTO.getMonthlyGrocery().getMonthlyGroceryId();
    }
}
