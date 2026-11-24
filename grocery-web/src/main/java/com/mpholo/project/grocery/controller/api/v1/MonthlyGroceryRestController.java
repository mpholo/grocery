package com.mpholo.project.grocery.controller.api.v1;

import com.mpholo.project.grocery.controller.api.v1.dto.CopyMonthlyGroceryRequest;
import com.mpholo.project.grocery.controller.api.v1.dto.MonthlyGrocerySummaryResponse;
import com.mpholo.project.grocery.controller.api.v1.dto.UpdateMonthlyGroceryRequest;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.model.MonthlyGroceryListDTO;
import com.mpholo.project.grocery.service.MonthlyGroceryService;
import com.mpholo.project.grocery.service.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController("MonthlyGroceryApi")
@RequestMapping("/api/v1/monthlygroceries/")
public class MonthlyGroceryRestController {

    private final MonthlyGroceryService monthlyGroceryService;


    public MonthlyGroceryRestController(MonthlyGroceryService monthlyGroceryService) {
        this.monthlyGroceryService = monthlyGroceryService;
    }

    @GetMapping
    public ResponseEntity<MonthlyGroceryListDTO> getAllMonthlyGrocery() {
        return new ResponseEntity<MonthlyGroceryListDTO>(
                new MonthlyGroceryListDTO(monthlyGroceryService.findAll()), HttpStatus.OK);

    }

    @GetMapping("summaries")
    public ResponseEntity<List<MonthlyGrocerySummaryResponse>> getMonthlyGrocerySummaries() {
        List<MonthlyGrocerySummaryResponse> summaries = monthlyGroceryService.findAll()
                .stream()
                .map(MonthlyGrocerySummaryResponse::from)
                .collect(Collectors.toList());
        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }

    @PostMapping("{monthlyGroceryId}/copy")
    public ResponseEntity<MonthlyGrocerySummaryResponse> copyMonthlyGrocery(
            @PathVariable Integer monthlyGroceryId,
            @RequestBody CopyMonthlyGroceryRequest request) {

        if (request.getMonth() == null || request.getMonth() < 1 || request.getMonth() > 12
                || request.getYear() == null) {
            return ResponseEntity.badRequest().build();
        }

        MonthlyGroceryDTO copied = monthlyGroceryService.copyToMonth(
                        monthlyGroceryId,
                        request.getMonth(),
                        request.getYear(),
                        request.getBudgetAmount())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Monthly grocery not found for id " + monthlyGroceryId));

        return new ResponseEntity<>(MonthlyGrocerySummaryResponse.from(copied), HttpStatus.CREATED);
    }

    @PatchMapping("{monthlyGroceryId}")
    public ResponseEntity<MonthlyGrocerySummaryResponse> updateMonthlyGrocery(
            @PathVariable Integer monthlyGroceryId,
            @RequestBody UpdateMonthlyGroceryRequest request) {

        if (request.getBudgetAmount() == null || request.getBudgetAmount() < 0) {
            return ResponseEntity.badRequest().build();
        }

        MonthlyGroceryDTO updated = monthlyGroceryService
                .updateBudget(monthlyGroceryId, request.getBudgetAmount())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Monthly grocery not found for id " + monthlyGroceryId));

        return new ResponseEntity<>(MonthlyGrocerySummaryResponse.from(updated), HttpStatus.OK);
    }

    @DeleteMapping("{monthlyGroceryId}")
    public ResponseEntity<Void> deleteMonthlyGrocery(@PathVariable Integer monthlyGroceryId) {
        boolean deleted = monthlyGroceryService.deleteIfBasketEmpty(monthlyGroceryId);
        if (!deleted) {
            // Has items currently in the basket — refuse to delete.
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{period}")
    public ResponseEntity<MonthlyGroceryDTO> getMonthlyGrocery(@PathVariable String period) {
        return  new ResponseEntity<MonthlyGroceryDTO>(
                monthlyGroceryService.findByPeriod(period),HttpStatus.OK);

    }

}
