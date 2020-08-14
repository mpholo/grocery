package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroceryBasketMapper {

    GroceryBasketMapper INSTANCE = Mappers.getMapper(GroceryBasketMapper.class);

    GroceryItemDTO groceryBasketToGroceryBasketDTO(GroceryItem groceryBasket);
    GroceryItem groceryBasketDTOToGroceryBasket(GroceryItemDTO groceryBasketDTO);


}
