package com.mpholo.project.grocery.controller.api.v1.dto;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Flat summary of a monthly grocery for the JSON API. Unlike
 * {@link MonthlyGroceryDTO} it omits the {@code groceryItems} collection,
 * so it is safe to serialise (no bidirectional recursion) and cheap for a
 * client that only needs to list and choose a month.
 */
public class MonthlyGrocerySummaryResponse implements Serializable {

    private Integer monthlyGroceryId;
    private double budgetAmount;
    private String period;
    private LocalDate startDate;
    private LocalDate endDate;
    // Money spent so far = total of items currently ticked as in-basket (purchased).
    private double spent;

    public MonthlyGrocerySummaryResponse() {
    }

    public static MonthlyGrocerySummaryResponse from(MonthlyGroceryDTO dto) {
        MonthlyGrocerySummaryResponse r = new MonthlyGrocerySummaryResponse();
        r.monthlyGroceryId = dto.getMonthlyGroceryId();
        r.budgetAmount = dto.getBudgetAmount();
        r.period = dto.getPeriod();
        r.startDate = dto.getStartDate();
        r.endDate = dto.getEndDate();
        r.spent = dto.getGroceryItems() == null ? 0.0 :
                dto.getGroceryItems().stream()
                        .filter(GroceryItem::isPurchased)
                        .mapToDouble(i -> i.getActualPrice() * i.getQuantity())
                        .sum();
        return r;
    }

    public Integer getMonthlyGroceryId() {
        return monthlyGroceryId;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public String getPeriod() {
        return period;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getSpent() {
        return spent;
    }
}
