package com.mpholo.project.grocery.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/18
 IDE IntelliJ IDEA
 *******************************************************************/

@Entity
@Data
@EqualsAndHashCode(exclude = {"products"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    @NotNull
    private String categoryName;

    @OneToMany(cascade= CascadeType.DETACH)
    @JoinColumn(name="category_id")
    private Set<Product> products = new HashSet<>();


}
