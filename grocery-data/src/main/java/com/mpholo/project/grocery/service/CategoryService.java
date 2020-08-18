package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.model.CategoryDTO;

import java.util.Optional;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/18
 IDE IntelliJ IDEA
 *******************************************************************/

public interface CategoryService extends CrudService<CategoryDTO,Integer> {

    Optional<CategoryDTO> findByCategoryName(String name);
}
