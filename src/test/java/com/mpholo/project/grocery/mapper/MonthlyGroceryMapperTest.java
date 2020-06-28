package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthlyGroceryMapperTest {

    MonthlyGroceryMapper monthlyGroceryMapper = MonthlyGroceryMapper.INSTACE;
    public static final int ID=1;
    public static final double AMOUNT =5000.00;


    @Test
    void monthlyGroceryToMontghlyGroceryDTO() {

        //given
        MonthlyGrocery monthlyGrocery = new MonthlyGrocery();
        monthlyGrocery.setBudgetAmount(AMOUNT);
        monthlyGrocery.setGroceryId(ID);

        //when
        MonthlyGroceryDTO monthlyGroceryDTO=monthlyGroceryMapper.monthlyGroceryToMonthlyGroceryDTO(monthlyGrocery);

        //then
        assertEquals(Integer.valueOf(ID),monthlyGroceryDTO.getGroceryId());
        assertEquals(AMOUNT,monthlyGrocery.getBudgetAmount());
    }

    @Test
    void monthlyGroceryDTOMontghlyGrocery() {


        //given
        MonthlyGroceryDTO monthlyGroceryDTO = new MonthlyGroceryDTO();
        monthlyGroceryDTO.setBudgetAmount(AMOUNT);
        monthlyGroceryDTO.setGroceryId(ID);

        //when
        MonthlyGrocery monthlyGrocery=monthlyGroceryMapper.monthlyGroceryDTOToMonthlyGrocery(monthlyGroceryDTO);

        //then
        assertEquals(Integer.valueOf(ID),monthlyGrocery.getGroceryId());
        assertEquals(AMOUNT,monthlyGrocery.getBudgetAmount());
    }
}