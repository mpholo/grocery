package com.mpholo.project.grocery.v1;

import com.mpholo.project.grocery.controller.api.v1.MonthlyGroceryRestController;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.service.MonthlyGroceryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MonthlyGroceryControllerTest {

    public static final String JULY_2020 = "July 2020";
    @InjectMocks
    MonthlyGroceryRestController monthlyGroceryController;

    @Mock
    MonthlyGroceryService monthlyGroceryService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() throws  Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(monthlyGroceryController).build();

    }

    @Test
    void testMonthlyGroceryLists() throws  Exception{

        //given
        List<MonthlyGroceryDTO> monthlyGroceryDTOList = Arrays.asList(new MonthlyGroceryDTO(),new MonthlyGroceryDTO(),new MonthlyGroceryDTO());

        //then
        when(monthlyGroceryService.findAll()).thenReturn(monthlyGroceryDTOList);

        //when
        mockMvc.perform(get("/api/v1/monthlygroceries/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyGroceries",hasSize(3)));

    }

    @Test
    void testFindMonthlyGroceryByPeriod() throws  Exception {

        //given
        MonthlyGroceryDTO monthlyGroceryDTO =new MonthlyGroceryDTO();
        monthlyGroceryDTO.setMonthlyGroceryId(1);
        monthlyGroceryDTO.setBudgetAmount(5000.00);
        monthlyGroceryDTO.setPeriod(JULY_2020);

        //when
        when(monthlyGroceryService.findByPeriod(anyString())).thenReturn(monthlyGroceryDTO);

        //then
        mockMvc.perform(get("/api/v1/monthlygroceries/"+JULY_2020)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period",equalTo(JULY_2020)));

    }
}