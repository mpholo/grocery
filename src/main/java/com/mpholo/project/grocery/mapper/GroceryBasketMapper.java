package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.GroceryBasket;
import com.mpholo.project.grocery.model.GroceryBasketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroceryBasketMapper {

    GroceryBasketMapper INSTANCE = Mappers.getMapper(GroceryBasketMapper.class);

    GroceryBasketDTO groceryBasketToGroceryBasketDTO(GroceryBasket groceryBasket);
    GroceryBasket groceryBasketDTOToGroceryBasket(GroceryBasketDTO groceryBasketDTO);


}
