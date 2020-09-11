package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.exceptions.NotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        MonthlyGroceryDTO monthlyGroceryDTO = monthlyGroceryService.findById(Integer.valueOf(monthlyGroceryId)).get();
        List<GroceryItemDTO> groceryItemList = groceryItemService.findByMonthlyGrocery(monthlyGroceryDTO);

        log.info("Total number of monthly grocery items for {} {}",monthlyGroceryDTO.getPeriod(),groceryItemList.size());

        MonthlyGrocery monthlyGrocery = MonthlyGroceryMapper.INSTACE.monthlyGroceryDTOToMonthlyGrocery(monthlyGroceryDTO);
        GroceryItemUpdateDTO item=new GroceryItemUpdateDTO();

       List<ProductDTO> productList = productService.findAll()
                .stream()
                .sorted(Comparator.comparing( p->p.getProductName()))
                .collect(Collectors.toList());
        log.info("displaying all products: "+productList.size());

        double totalPrice=0.0;
        int totalQuanty=0;
        double finalTotalPrice=0.0;

        for (GroceryItemDTO i:groceryItemList) {
            totalPrice+=i.getActualPrice();
            totalQuanty+=i.getQuantity();
            finalTotalPrice+=(i.getActualPrice()*i.getQuantity());
        }
        log.info("calculated totals totalprice {} totalQuantity {} finalTotalPrice {}",totalPrice,totalQuanty,finalTotalPrice);

        model.addAttribute(MonthlyGroceryAttributeNames.MONTHLY_GROCERY,monthlyGroceryDTO);
        model.addAttribute(GroceryItemAttributeNames.GROCERY_ITEM_LIST,groceryItemList);
        model.addAttribute(ProductAttributeNames.PRODUCT_LIST,productList);
        model.addAttribute(GroceryItemAttributeNames.GROCERY_ITEM,item);

        model.addAttribute(GroceryItemAttributeNames.TOTAL_PRICE,totalPrice);
        model.addAttribute(GroceryItemAttributeNames.TOTAL_QUANTITY,totalQuanty);
        model.addAttribute(GroceryItemAttributeNames.FINAL_TOTAL_PRICE,finalTotalPrice);


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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormat(Exception exception) {

        log.error("Handle Number Format Exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("400error");
        modelAndView.addObject("exception",exception);

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFound(Exception exception) {

        log.error("Handling not found Exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("404error");
        modelAndView.addObject("exception",exception);

        return modelAndView;
    }
}
