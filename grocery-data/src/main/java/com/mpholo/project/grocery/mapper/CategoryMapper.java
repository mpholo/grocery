package com.mpholo.project.grocery.mapper;

import com.mpholo.project.grocery.domain.Category;
import com.mpholo.project.grocery.model.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/18
 IDE IntelliJ IDEA
 *******************************************************************/

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO CategoryToCategoryDTO(Category category);
    Category CategoryDTOToCategory(CategoryDTO categoryDTO);


}
