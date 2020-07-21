package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.GroceryBasket;
import com.mpholo.project.grocery.mapper.GroceryBasketMapper;
import com.mpholo.project.grocery.model.GroceryBasketDTO;
import com.mpholo.project.grocery.repositories.GroceryBasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class GroceryBasketServiceImplTest {

    public static final int ID=1;
    public static final double ACTUAL_AMOUNT=5500.00;


    GroceryBasketService groceryBasketService;

    @Mock
    GroceryBasketRepository groceryBasketRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        groceryBasketService = new GroceryBasketServiceImpl(groceryBasketRepository, GroceryBasketMapper.INSTANCE);

    }


    @Test
    void testCreateGroceryBasketItem() {

        //given
        GroceryBasket groceryBasket = new GroceryBasket();
        groceryBasket.setGroceryBasketId(ID);
        groceryBasket.setActualPrice(ACTUAL_AMOUNT);

        when(groceryBasketRepository.findById(anyInt())).thenReturn(Optional.of(groceryBasket));

        //when
        Optional<GroceryBasketDTO> groceryBasketDTO = groceryBasketService.findById(ID);

        //then
        assertEquals(ID,groceryBasketDTO.get().getGroceryBasketId());
    }


    @Test
    void getGroceryBasketLis() {

        //given
        List<GroceryBasket> groceryBasketList = Arrays.asList(new GroceryBasket(),new GroceryBasket(),new GroceryBasket());

        when(groceryBasketRepository.findAll()).thenReturn(groceryBasketList);


        //when
        List<GroceryBasketDTO> groceryBasketDTOS = groceryBasketService.findAll();

        //then
        assertEquals(3,groceryBasketDTOS.size());
    }
}