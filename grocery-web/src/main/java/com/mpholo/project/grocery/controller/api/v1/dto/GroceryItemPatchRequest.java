package com.mpholo.project.grocery.controller.api.v1.dto;

import java.io.Serializable;

/**
 * Partial-update request for a grocery item. All fields are optional
 * (wrapper types): only the ones supplied in the JSON body are applied,
 * so a client can change quantity, price and/or the "added to basket"
 * (purchased) flag independently.
 */
public class GroceryItemPatchRequest implements Serializable {

    private Integer quantity;
    private Double actualPrice;
    private Boolean purchased;

    public GroceryItemPatchRequest() {
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Boolean getPurchased() {
        return purchased;
    }

    public void setPurchased(Boolean purchased) {
        this.purchased = purchased;
    }
}
