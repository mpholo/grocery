package com.mpholo.project.grocery.api.v1;

import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.model.MonthlyGroceryListDTO;
import com.mpholo.project.grocery.service.MonthlyGroceryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("MonthlyGroceryApi")
@RequestMapping("/api/v1/monthlygroceries/")
public class MonthlyGroceryController {

    private final MonthlyGroceryService monthlyGroceryService;


    public MonthlyGroceryController(MonthlyGroceryService monthlyGroceryService) {
        this.monthlyGroceryService = monthlyGroceryService;
    }

    @GetMapping
    public ResponseEntity<MonthlyGroceryListDTO> getAllMonthlyGrocery() {
        return new ResponseEntity<MonthlyGroceryListDTO>(
                new MonthlyGroceryListDTO(monthlyGroceryService.findAll()), HttpStatus.OK);

    }

    @GetMapping("{period}")
    public ResponseEntity<MonthlyGroceryDTO> getMonthlyGrocery(@PathVariable String period) {
        return  new ResponseEntity<MonthlyGroceryDTO>(
                monthlyGroceryService.findByPeriod(period),HttpStatus.OK);

    }

}
