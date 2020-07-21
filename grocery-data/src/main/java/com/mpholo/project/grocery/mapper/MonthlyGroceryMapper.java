package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MonthlyGroceryMapper {

    MonthlyGroceryMapper INSTACE = Mappers.getMapper(MonthlyGroceryMapper.class);

    MonthlyGroceryDTO monthlyGroceryToMonthlyGroceryDTO(MonthlyGrocery monthlyGrocery);
    MonthlyGrocery monthlyGroceryDTOToMonthlyGrocery(MonthlyGroceryDTO monthlyGroceryDTO);
}
