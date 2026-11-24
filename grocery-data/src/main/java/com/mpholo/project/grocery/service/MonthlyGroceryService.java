package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.model.MonthlyGroceryDTO;

import java.util.List;
import java.util.Optional;

public interface MonthlyGroceryService extends CrudService<MonthlyGroceryDTO,Integer> {

    MonthlyGroceryDTO findByPeriod(String period);
    List<MonthlyGroceryDTO> findByYear(Integer year);
    Optional<MonthlyGroceryDTO> copy(Integer monthlyGroceryId);

    /**
     * Copy a monthly grocery (with all its items) into the given month/year.
     * The copied items are reset to "not in basket" (purchased = false).
     *
     * @param month 1-12
     * @param year  e.g. 2026
     * @param budgetAmount budget for the new month; if null the source budget is reused
     */
    Optional<MonthlyGroceryDTO> copyToMonth(Integer monthlyGroceryId, int month, int year, Double budgetAmount);

}
