package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.service.GroceryItemService;
import com.mpholo.project.grocery.service.MonthlyGroceryService;
import com.mpholo.project.grocery.util.GroceryItemAttributeNames;
import com.mpholo.project.grocery.util.GroceryItemMappings;
import com.mpholo.project.grocery.util.GroceryItemViewNames;
import com.mpholo.project.grocery.util.MonthlyGroceryAttributeNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    public GroceryItemController(GroceryItemService groceryItemService, MonthlyGroceryService monthlyGroceryService) {
        this.groceryItemService = groceryItemService;
        this.monthlyGroceryService = monthlyGroceryService;
    }

    @GetMapping(GroceryItemMappings.GROCERY_ITEM_LIST)
    public String displayGroceryItems(Model model, @RequestParam(name="monthlyGroceryId") int monthlyGroceryId ) {

        MonthlyGroceryDTO monthlyGroceryDTO = monthlyGroceryService.findById(monthlyGroceryId).get();
        List<GroceryItemDTO> groceryItemList = groceryItemService.findByMonthlyGrocery(monthlyGroceryDTO);
        log.info("Total number of monthly grocery items for {} {}",monthlyGroceryDTO.getPeriod(),groceryItemList.size());

        model.addAttribute(MonthlyGroceryAttributeNames.MONTHLY_GROCERY,monthlyGroceryDTO);
        model.addAttribute(GroceryItemAttributeNames.GROCERY_ITEM_LIST,groceryItemList);

        return GroceryItemViewNames.GROCERY_ITEMS;
    }

    @GetMapping(GroceryItemMappings.GROCERY_ITEM_DELETE)
    public String deleteGroceryItem(@RequestParam(name="groceryItemId") int groceryItemId,
                                    @RequestParam(name="monthGroceryId") int monthlyGroceryId) {
        groceryItemService.deleteById(groceryItemId);
        log.info("deleted grocery item {}",groceryItemId);
        return "redirect:"+GROCERY_ITEM_REDIRECT_LIST+monthlyGroceryId;
    }
}
