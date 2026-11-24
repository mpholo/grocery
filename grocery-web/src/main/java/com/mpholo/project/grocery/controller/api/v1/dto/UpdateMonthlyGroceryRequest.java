package com.mpholo.project.grocery.controller.api.v1.dto;

import java.io.Serializable;

/**
 * Request body for updating a monthly grocery via
 * {@code PATCH /api/v1/monthlygroceries/{id}}. Currently only the budget amount
 * is editable; kept as its own DTO so more editable fields can be added later.
 */
public class UpdateMonthlyGroceryRequest implements Serializable {

    private Double budgetAmount;

    public UpdateMonthlyGroceryRequest() {
    }

    public Double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(Double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
}
