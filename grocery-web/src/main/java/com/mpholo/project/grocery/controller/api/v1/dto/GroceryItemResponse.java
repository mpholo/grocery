package com.mpholo.project.grocery.controller.api.v1.dto;

import com.mpholo.project.grocery.model.GroceryItemDTO;

import java.io.Serializable;

/**
 * Flat, client-friendly view of a grocery item for the JSON API.
 * <p>
 * The internal {@link GroceryItemDTO} embeds the full JPA {@code Product} and
 * {@code MonthlyGrocery} entities, which hold bidirectional collections and
 * would recurse when serialised to JSON. This DTO exposes only what a client
 * (e.g. the Android app) needs, as primitives.
 */
public class GroceryItemResponse implements Serializable {

    private int groceryItemId;
    private int quantity;
    private double actualPrice;
    private boolean purchased;
    private double totalPrice;
    private Integer productId;
    private String productName;
    private int monthlyGroceryId;
    private String period;

    public GroceryItemResponse() {
    }

    public static GroceryItemResponse from(GroceryItemDTO dto) {
        GroceryItemResponse r = new GroceryItemResponse();
        r.groceryItemId = dto.getGroceryItemId();
        r.quantity = dto.getQuantity();
        r.actualPrice = dto.getActualPrice();
        r.purchased = dto.isPurchased();
        r.totalPrice = dto.getActualPrice() * dto.getQuantity();
        if (dto.getProduct() != null) {
            r.productId = dto.getProduct().getProductId();
            r.productName = dto.getProduct().getProductName();
        }
        if (dto.getMonthlyGrocery() != null) {
            r.monthlyGroceryId = dto.getMonthlyGrocery().getMonthlyGroceryId();
            r.period = dto.getMonthlyGrocery().getPeriod();
        }
        return r;
    }

    public int getGroceryItemId() {
        return groceryItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getMonthlyGroceryId() {
        return monthlyGroceryId;
    }

    public String getPeriod() {
        return period;
    }
}
