package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-08-29T05:55:53+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class GroceryBasketMapperImpl implements GroceryBasketMapper {

    @Override
    public GroceryItemDTO groceryBasketToGroceryBasketDTO(GroceryItem groceryBasket) {
        if ( groceryBasket == null ) {
            return null;
        }

        GroceryItemDTO groceryItemDTO = new GroceryItemDTO();

        if ( groceryBasket.getGroceryItemId() != null ) {
            groceryItemDTO.setGroceryItemId( groceryBasket.getGroceryItemId() );
        }
        groceryItemDTO.setQuantity( groceryBasket.getQuantity() );
        groceryItemDTO.setActualPrice( groceryBasket.getActualPrice() );
        groceryItemDTO.setProduct( groceryBasket.getProduct() );
        groceryItemDTO.setMonthlyGrocery( groceryBasket.getMonthlyGrocery() );

        return groceryItemDTO;
    }

    @Override
    public GroceryItem groceryBasketDTOToGroceryBasket(GroceryItemDTO groceryBasketDTO) {
        if ( groceryBasketDTO == null ) {
            return null;
        }

        GroceryItem groceryItem = new GroceryItem();

        groceryItem.setGroceryItemId( groceryBasketDTO.getGroceryItemId() );
        groceryItem.setQuantity( groceryBasketDTO.getQuantity() );
        groceryItem.setActualPrice( groceryBasketDTO.getActualPrice() );
        groceryItem.setProduct( groceryBasketDTO.getProduct() );
        groceryItem.setMonthlyGrocery( groceryBasketDTO.getMonthlyGrocery() );

        return groceryItem;
    }
}
