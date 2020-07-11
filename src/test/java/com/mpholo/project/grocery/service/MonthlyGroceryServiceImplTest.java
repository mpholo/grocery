package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.mapper.MonthlyGroceryMapper;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.repositories.MonthlyGroceryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MonthlyGroceryServiceImplTest {

    private static final int ID=1;
    private static final double AMOUNT=4000.00;
    private  static final String PERIOD="July 2020";

    MonthlyGroceryService monthlyGroceryService;

    @Mock
    MonthlyGroceryRepository monthlyGroceryRepository;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        monthlyGroceryService = new MonthlyGroceryServiceImpl(monthlyGroceryRepository, MonthlyGroceryMapper.INSTACE);
    }


    @Test
    void testCreateMonthlyGrocery() {

        //given
        MonthlyGrocery monthlyGrocery = new MonthlyGrocery();
        monthlyGrocery.setMonthlyGroceryId(ID);
        monthlyGrocery.setBudgetAmount(AMOUNT);
        monthlyGrocery.setPeriod(PERIOD);
        when(monthlyGroceryRepository.findByPeriod(PERIOD)).thenReturn(monthlyGrocery);


        //when
        MonthlyGroceryDTO monthlyGroceryDTO = monthlyGroceryService.findByPeriod(PERIOD);

        //then
        assertEquals(PERIOD,monthlyGroceryDTO.getPeriod());
    }

    @Test
    void getMonthlyGroceryList() {

        //given
        List<MonthlyGrocery> monthlyGroceryList = Arrays.asList(new MonthlyGrocery(),new MonthlyGrocery());

        when(monthlyGroceryRepository.findAll()).thenReturn(monthlyGroceryList);

        //when
        List<MonthlyGroceryDTO> monthlyGroceryDTOS = monthlyGroceryService.findAll();

        //then
        assertEquals(2,monthlyGroceryDTOS.size());
    }
}