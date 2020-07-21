package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.GroceryBasket;
import com.mpholo.project.grocery.model.GroceryBasketDTO;
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
        GroceryBasket groceryBasket = new GroceryBasket();
        groceryBasket.setGroceryBasketId(ID);
        groceryBasket.setActualPrice(ACTUAL_AMOUNT);

        //when
        GroceryBasketDTO groceryBasketDTO=groceryBasketMapper.groceryBasketToGroceryBasketDTO(groceryBasket);

        //then
        assertEquals(Integer.valueOf(ID),groceryBasketDTO.getGroceryBasketId());
        assertEquals(ACTUAL_AMOUNT,groceryBasketDTO.getActualPrice());
    }

    @Test
    void groceryBasketDTOToGroceryBasket() {

        //given
        GroceryBasketDTO groceryBasketDTO = new GroceryBasketDTO();
        groceryBasketDTO.setGroceryBasketId(ID);
        groceryBasketDTO.setActualPrice(ACTUAL_AMOUNT);

        //when
        GroceryBasket groceryBasket = groceryBasketMapper.groceryBasketDTOToGroceryBasket(groceryBasketDTO);

        //then
        assertEquals(Integer.valueOf(ID),groceryBasket.getGroceryBasketId());
        assertEquals(ACTUAL_AMOUNT,groceryBasket.getActualPrice());

    }
}