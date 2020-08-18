package com.mpholo.project.grocery.repositories;

import com.mpholo.project.grocery.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/18
 IDE IntelliJ IDEA
 *******************************************************************/

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Optional<Category> findByCategoryName(String name);
}
