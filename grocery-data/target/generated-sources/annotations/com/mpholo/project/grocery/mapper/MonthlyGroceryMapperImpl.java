package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-24T12:08:52+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.4 (Oracle Corporation)"
)
@Component
public class MonthlyGroceryMapperImpl implements MonthlyGroceryMapper {

    @Override
    public MonthlyGroceryDTO monthlyGroceryToMonthlyGroceryDTO(MonthlyGrocery monthlyGrocery) {
        if ( monthlyGrocery == null ) {
            return null;
        }

        MonthlyGroceryDTO monthlyGroceryDTO = new MonthlyGroceryDTO();

        monthlyGroceryDTO.setMonthlyGroceryId( monthlyGrocery.getMonthlyGroceryId() );
        monthlyGroceryDTO.setBudgetAmount( monthlyGrocery.getBudgetAmount() );
        monthlyGroceryDTO.setPeriod( monthlyGrocery.getPeriod() );
        monthlyGroceryDTO.setStartDate( monthlyGrocery.getStartDate() );
        monthlyGroceryDTO.setEndDate( monthlyGrocery.getEndDate() );
        Set<GroceryItem> set = monthlyGrocery.getGroceryItems();
        if ( set != null ) {
            monthlyGroceryDTO.setGroceryItems( new HashSet<GroceryItem>( set ) );
        }

        return monthlyGroceryDTO;
    }

    @Override
    public MonthlyGrocery monthlyGroceryDTOToMonthlyGrocery(MonthlyGroceryDTO monthlyGroceryDTO) {
        if ( monthlyGroceryDTO == null ) {
            return null;
        }

        MonthlyGrocery monthlyGrocery = new MonthlyGrocery();

        if ( monthlyGroceryDTO.getMonthlyGroceryId() != null ) {
            monthlyGrocery.setMonthlyGroceryId( monthlyGroceryDTO.getMonthlyGroceryId() );
        }
        monthlyGrocery.setBudgetAmount( monthlyGroceryDTO.getBudgetAmount() );
        monthlyGrocery.setPeriod( monthlyGroceryDTO.getPeriod() );
        monthlyGrocery.setStartDate( monthlyGroceryDTO.getStartDate() );
        monthlyGrocery.setEndDate( monthlyGroceryDTO.getEndDate() );
        Set<GroceryItem> set = monthlyGroceryDTO.getGroceryItems();
        if ( set != null ) {
            monthlyGrocery.setGroceryItems( new HashSet<GroceryItem>( set ) );
        }

        return monthlyGrocery;
    }
}
