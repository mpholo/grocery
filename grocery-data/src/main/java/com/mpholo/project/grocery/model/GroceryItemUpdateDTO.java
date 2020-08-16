package com.mpholo.project.grocery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/16
 IDE IntelliJ IDEA
 *******************************************************************/

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"product","monthlyGrocery"})
public class GroceryItemUpdateDTO implements Serializable {

    private int groceryItemId;
    private int quantity=1;
    private double actualPrice;
    private int productId;
    private int monthlyGroceryId;
}
