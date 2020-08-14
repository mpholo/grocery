package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroceryBasketMapperTest {

    GroceryBasketMapper groceryBasketMapper = GroceryBasketMapper.INSTANCE;
    public static final int ID=1;
    public static final int MONTHLY_GROCERY_ID=1;
    public static final double ACTUAL_AMOUNT=5500.00;


    @Test
    void groceryBasketToGroceryBasketDTO() {

        //given
        GroceryItem groceryBasket = new GroceryItem();
        groceryBasket.setGroceryItemId(ID);
        groceryBasket.setActualPrice(ACTUAL_AMOUNT);

        //when
        GroceryItemDTO groceryBasketDTO=groceryBasketMapper.groceryBasketToGroceryBasketDTO(groceryBasket);

        //then
        assertEquals(Integer.valueOf(ID),groceryBasketDTO.getGroceryItemId());
        assertEquals(ACTUAL_AMOUNT,groceryBasketDTO.getActualPrice());
    }

    @Test
    void groceryBasketDTOToGroceryBasket() {

        //given
        GroceryItemDTO groceryBasketDTO = new GroceryItemDTO();
        groceryBasketDTO.setGroceryItemId(ID);
        groceryBasketDTO.setActualPrice(ACTUAL_AMOUNT);

        //when
        GroceryItem groceryBasket = groceryBasketMapper.groceryBasketDTOToGroceryBasket(groceryBasketDTO);

        //then
        assertEquals(Integer.valueOf(ID),groceryBasket.getGroceryItemId());
        assertEquals(ACTUAL_AMOUNT,groceryBasket.getActualPrice());

    }
}