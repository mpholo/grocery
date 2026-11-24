package com.mpholo.project.grocery.controller.api.v1.dto;

import java.io.Serializable;

/**
 * Request body for copying a monthly grocery into a chosen month/year.
 * {@code budgetAmount} is optional; when null the source month's budget is reused.
 */
public class CopyMonthlyGroceryRequest implements Serializable {

    private Integer month;   // 1-12
    private Integer year;    // e.g. 2026
    private Double budgetAmount;

    public CopyMonthlyGroceryRequest() {
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(Double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
}
