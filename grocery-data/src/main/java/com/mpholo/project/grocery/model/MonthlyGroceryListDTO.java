package com.mpholo.project.grocery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyGroceryListDTO implements Serializable  {

    List<MonthlyGroceryDTO> monthlyGroceries;


}
