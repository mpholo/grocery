package com.mpholo.project.grocery.android.data

import com.mpholo.project.grocery.android.data.api.GroceryApi
import com.mpholo.project.grocery.android.data.api.NetworkModule
import com.mpholo.project.grocery.android.data.model.CopyMonthRequest
import com.mpholo.project.grocery.android.data.model.CreateProductRequest
import com.mpholo.project.grocery.android.data.model.GroceryItem
import com.mpholo.project.grocery.android.data.model.GroceryItemPatchRequest
import com.mpholo.project.grocery.android.data.model.MonthSummary
import com.mpholo.project.grocery.android.data.model.Product

/** Single point of access to the backend for the ViewModels. */
class GroceryRepository(
    private val api: GroceryApi = NetworkModule.groceryApi
) {

    suspend fun getMonths(): List<MonthSummary> = api.getMonths()

    suspend fun getItems(monthlyGroceryId: Int): List<GroceryItem> =
        api.getItems(monthlyGroceryId)

    /** Toggle the "added to basket" (purchased) flag. */
    suspend fun setPurchased(groceryItemId: Int, purchased: Boolean): GroceryItem =
        api.patchItem(groceryItemId, GroceryItemPatchRequest(purchased = purchased))

    /** Change quantity and/or unit price. Pass null to leave a field unchanged. */
    suspend fun updateItem(
        groceryItemId: Int,
        quantity: Int? = null,
        actualPrice: Double? = null
    ): GroceryItem =
        api.patchItem(
            groceryItemId,
            GroceryItemPatchRequest(quantity = quantity, actualPrice = actualPrice)
        )

    /** Copy a month (with its items) into [month]/[year]; items reset to not-in-basket. */
    suspend fun copyMonth(sourceMonthlyGroceryId: Int, month: Int, year: Int): MonthSummary =
        api.copyMonth(sourceMonthlyGroceryId, CopyMonthRequest(month = month, year = year))

    suspend fun getProducts(): List<Product> = api.getProducts().products

    suspend fun addProduct(name: String, description: String?, price: Double): Product =
        api.createProduct(CreateProductRequest(name, description, price))
}
