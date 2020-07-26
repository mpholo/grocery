package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.GroceryBasket;
import com.mpholo.project.grocery.model.GroceryBasketDTO;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-07-26T06:04:09+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class GroceryBasketMapperImpl implements GroceryBasketMapper {

    @Override
    public GroceryBasketDTO groceryBasketToGroceryBasketDTO(GroceryBasket groceryBasket) {
        if ( groceryBasket == null ) {
            return null;
        }

        GroceryBasketDTO groceryBasketDTO = new GroceryBasketDTO();

        if ( groceryBasket.getGroceryBasketId() != null ) {
            groceryBasketDTO.setGroceryBasketId( groceryBasket.getGroceryBasketId() );
        }
        groceryBasketDTO.setQuantity( groceryBasket.getQuantity() );
        groceryBasketDTO.setActualPrice( groceryBasket.getActualPrice() );
        groceryBasketDTO.setMonthlyGrocery( groceryBasket.getMonthlyGrocery() );

        return groceryBasketDTO;
    }

    @Override
    public GroceryBasket groceryBasketDTOToGroceryBasket(GroceryBasketDTO groceryBasketDTO) {
        if ( groceryBasketDTO == null ) {
            return null;
        }

        GroceryBasket groceryBasket = new GroceryBasket();

        groceryBasket.setGroceryBasketId( groceryBasketDTO.getGroceryBasketId() );
        groceryBasket.setQuantity( groceryBasketDTO.getQuantity() );
        groceryBasket.setActualPrice( groceryBasketDTO.getActualPrice() );
        groceryBasket.setMonthlyGrocery( groceryBasketDTO.getMonthlyGrocery() );

        return groceryBasket;
    }
}
