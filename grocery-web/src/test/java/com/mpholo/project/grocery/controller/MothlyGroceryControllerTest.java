package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.service.MonthlyGroceryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.mpholo.project.grocery.util.MonthylGroceryMappings.MONTHLY_GROCERY_REDIRECT_LIST;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/10/12
 IDE IntelliJ IDEA
 *******************************************************************/

class MothlyGroceryControllerTest {

    @Mock
    MonthlyGroceryService monthlyGroceryService;

    MothlyGroceryController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        controller = new MothlyGroceryController(monthlyGroceryService);
        mockMvc= MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

    }

    @Test
    void displayMonthlyGroceries() {
        //TODO add test to display monthly groceries
    }

    @Test
    void saveMonthlyGrocery() {
        //TODO add test for saving mothly grocery
    }

    @Test
    void deleteMonthlyGrocery() throws Exception {

        mockMvc.perform(get("/monthly-grocery/delete?monthlyGroceryId=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"+MONTHLY_GROCERY_REDIRECT_LIST));
    }

    @Test
    void copyMonthlyGrocery() throws Exception {

        //given
        MonthlyGroceryDTO monthlyGroceryDTO = new MonthlyGroceryDTO();
        monthlyGroceryDTO.setMonthlyGroceryId(1);

        //when
        when(monthlyGroceryService.copy(anyInt())).thenReturn(Optional.of(monthlyGroceryDTO));

        //then
        mockMvc.perform(get("/monthly-grocery/copy?monthlyGroceryId=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:"+MONTHLY_GROCERY_REDIRECT_LIST));
    }
}