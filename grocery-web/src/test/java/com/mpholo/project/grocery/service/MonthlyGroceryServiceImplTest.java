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
import static org.mockito.Mockito.*;

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
        MonthlyGrocery monthlyGrocery = getRecord();
        when(monthlyGroceryRepository.save(any())).thenReturn(monthlyGrocery);


        //when
        MonthlyGroceryDTO monthlyGroceryDTO = monthlyGroceryService.save(MonthlyGroceryMapper.INSTACE.monthlyGroceryToMonthlyGroceryDTO(monthlyGrocery));

        //then
        assertEquals(PERIOD,monthlyGroceryDTO.getPeriod());
        verify(monthlyGroceryRepository,times(1)).save(any(MonthlyGrocery.class));
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

    @Test
    void updateMonthlyGroceryTest() {


        //given
        MonthlyGrocery monthlyGrocery = getRecord();

        String updatedPeriod ="December 2021";
        monthlyGrocery.setBudgetAmount(1000.00);
        monthlyGrocery.setPeriod(updatedPeriod);

        when(monthlyGroceryRepository.save(any())).thenReturn(monthlyGrocery);

        //when
        MonthlyGroceryDTO savedMonthlyGroceryDTO = monthlyGroceryService.edit(monthlyGrocery.getMonthlyGroceryId(),
                MonthlyGroceryMapper.INSTACE.monthlyGroceryToMonthlyGroceryDTO(monthlyGrocery));

        //then
        assertEquals(updatedPeriod,savedMonthlyGroceryDTO.getPeriod());
       verify(monthlyGroceryRepository,times(1)).save(any(MonthlyGrocery.class));
    }

    @Test
    void deleteMonthlyGroceryListTest() {

        //given
        Integer idToDelete = Integer.valueOf(1);

        //when
        monthlyGroceryService.deleteById(idToDelete);
        //no, when since method returns void

        //then
        verify(monthlyGroceryRepository,times(1)).deleteById(anyInt());
    }

    private MonthlyGrocery getRecord() {
        MonthlyGrocery monthlyGrocery = new MonthlyGrocery();
        monthlyGrocery.setMonthlyGroceryId(ID);
        monthlyGrocery.setBudgetAmount(AMOUNT);
        monthlyGrocery.setPeriod(PERIOD);

        return  monthlyGrocery;
    }

}