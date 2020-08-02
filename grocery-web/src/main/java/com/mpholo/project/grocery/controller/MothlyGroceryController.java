package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.service.MonthlyGroceryService;
import com.mpholo.project.grocery.util.MonthlyGroceryAttributeNames;
import com.mpholo.project.grocery.util.MonthylGroceryMappings;
import com.mpholo.project.grocery.util.MonthylGroceryViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/07/24 
 IDE IntelliJ IDEA
 *******************************************************************/

@Slf4j
@Controller
public class MothlyGroceryController {

    private final MonthlyGroceryService monthlyGroceryService;

    public MothlyGroceryController(MonthlyGroceryService monthlyGroceryService) {
        this.monthlyGroceryService = monthlyGroceryService;
    }


    @GetMapping({MonthylGroceryMappings.MONTHLY_GROCERY_LIST})
    public String displayMonthlyGroceries(Model model, @RequestParam(name="year",required = false) Integer year ) {

        log.info("getting monthly groceries");
        List<MonthlyGroceryDTO> monthlyGroceryDTOs= monthlyGroceryService.findByYear(year);
        model.addAttribute(MonthlyGroceryAttributeNames.MONTHLY_GROCERY_LIST,monthlyGroceryDTOs);

        //create new monthly grocery and set period
        MonthlyGroceryDTO newMonthlyGrocery = new MonthlyGroceryDTO();

        int grocery_year = monthlyGroceryDTOs.get(0).getEndDate().getYear();
        model.addAttribute("year",grocery_year);
        model.addAttribute(MonthlyGroceryAttributeNames.MONTHLY_GROCERY,newMonthlyGrocery);

        return MonthylGroceryViewNames.MONTHLY_GROCERIES;

    }

    @PostMapping(MonthylGroceryMappings.MONTHLY_GROCERY_SAVE)
    public String saveMonthlyGrocery(@ModelAttribute MonthlyGroceryDTO monthlyGroceryDTO) {
        monthlyGroceryService.save(monthlyGroceryDTO);
        return "redirect:"+MonthylGroceryMappings.MONTHLY_GROCERY_LIST;
    }

}
