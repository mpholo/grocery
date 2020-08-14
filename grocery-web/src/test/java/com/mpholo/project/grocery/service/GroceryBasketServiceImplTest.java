package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.mapper.GroceryBasketMapper;
import com.mpholo.project.grocery.mapper.MonthlyGroceryMapper;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.repositories.GroceryItemRepository;
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


    GroceryItemService groceryBasketService;

    @Mock
    GroceryItemRepository groceryBasketRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        groceryBasketService = new GroceryItemServiceImpl(groceryBasketRepository, GroceryBasketMapper.INSTANCE, MonthlyGroceryMapper.INSTACE);

    }


    @Test
    void testCreateGroceryBasketItem() {

        //given
        GroceryItem groceryBasket = new GroceryItem();
        groceryBasket.setGroceryItemId(ID);
        groceryBasket.setActualPrice(ACTUAL_AMOUNT);

        when(groceryBasketRepository.findById(anyInt())).thenReturn(Optional.of(groceryBasket));

        //when
        Optional<GroceryItemDTO> groceryBasketDTO = groceryBasketService.findById(ID);

        //then
        assertEquals(ID,groceryBasketDTO.get().getGroceryItemId());
    }


    @Test
    void getGroceryBasketLis() {

        //given
        List<GroceryItem> groceryBasketList = Arrays.asList(new GroceryItem(),new GroceryItem(),new GroceryItem());

        when(groceryBasketRepository.findAll()).thenReturn(groceryBasketList);


        //when
        List<GroceryItemDTO> groceryBasketDTOS = groceryBasketService.findAll();

        //then
        assertEquals(3,groceryBasketDTOS.size());
    }
}