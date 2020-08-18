package com.mpholo.project.grocery.model;

import com.mpholo.project.grocery.domain.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/18
 IDE IntelliJ IDEA
 *******************************************************************/

@Data
@EqualsAndHashCode(exclude = {"products"})
public class CategoryDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    private String categoryName;

    @OneToMany(cascade= CascadeType.DETACH)
    @JoinColumn(name="category_id")
    private Set<Product> products = new HashSet<>();




}
