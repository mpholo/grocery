package com.mpholo.project.grocery.android.data

import com.mpholo.project.grocery.android.data.api.GroceryApi
import com.mpholo.project.grocery.android.data.api.NetworkModule
import com.mpholo.project.grocery.android.data.model.CopyMonthRequest
import com.mpholo.project.grocery.android.data.model.CreateGroceryItemRequest
import com.mpholo.project.grocery.android.data.model.CreateProductRequest
import com.mpholo.project.grocery.android.data.model.GroceryItem
import com.mpholo.project.grocery.android.data.model.GroceryItemPatchRequest
import com.mpholo.project.grocery.android.data.model.MonthSummary
import com.mpholo.project.grocery.android.data.model.Product
import com.mpholo.project.grocery.android.data.model.UpdateBudgetRequest

/** Single point of access to the backend for the ViewModels. */
class GroceryRepository(
    private val api: GroceryApi = NetworkModule.groceryApi
) {

    suspend fun getMonths(): List<MonthSummary> = api.getMonths()

    suspend fun getItems(monthlyGroceryId: Int): List<GroceryItem> =
        api.getItems(monthlyGroceryId)

    /** Add a catalog product to a month as a new grocery item. */
    suspend fun addItemToMonth(
        monthlyGroceryId: Int,
        productId: Int,
        quantity: Int,
        actualPrice: Double
    ): GroceryItem =
        api.createItem(
            CreateGroceryItemRequest(
                monthlyGroceryId = monthlyGroceryId,
                productId = productId,
                quantity = quantity,
                actualPrice = actualPrice
            )
        )

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

    /** Update the budget amount for a month. */
    suspend fun updateBudget(monthlyGroceryId: Int, budgetAmount: Double): MonthSummary =
        api.updateMonth(monthlyGroceryId, UpdateBudgetRequest(budgetAmount))

    /** Delete a month (and its items). Fails if the month has items in the basket. */
    suspend fun deleteMonth(monthlyGroceryId: Int) {
        val response = api.deleteMonth(monthlyGroceryId)
        if (response.code() == 409) {
            throw IllegalStateException("Can't delete — this month has items in the basket.")
        }
        if (!response.isSuccessful) {
            throw IllegalStateException("Delete failed (${response.code()})")
        }
    }

    /** Remove a grocery item from its month. */
    suspend fun deleteItem(groceryItemId: Int) {
        val response = api.deleteItem(groceryItemId)
        if (!response.isSuccessful) {
            throw IllegalStateException("Delete failed (${response.code()})")
        }
    }

    suspend fun getProducts(): List<Product> = api.getProducts().products

    suspend fun addProduct(name: String, description: String?, price: Double): Product =
        api.createProduct(CreateProductRequest(name, description, price))

    /** Update an existing product's name, description and price. */
    suspend fun updateProduct(id: Int, name: String, description: String?, price: Double): Product =
        api.updateProduct(
            id,
            Product(
                productId = id,
                productName = name,
                productDescription = description,
                productPrice = price
            )
        )

    /** Delete a catalog product. Fails with a clear message if it's still used in a month. */
    suspend fun deleteProduct(productId: Int) {
        val response = api.deleteProduct(productId)
        if (response.code() == 409) {
            throw IllegalStateException("Can't delete — this product is used in a month.")
        }
        if (!response.isSuccessful) {
            throw IllegalStateException("Delete failed (${response.code()})")
        }
    }
}
