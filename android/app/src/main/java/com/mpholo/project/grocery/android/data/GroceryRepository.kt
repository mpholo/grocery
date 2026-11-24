package com.mpholo.project.grocery.android.data

import com.mpholo.project.grocery.android.data.api.GroceryApi
import com.mpholo.project.grocery.android.data.api.NetworkModule
import com.mpholo.project.grocery.android.data.local.LocalStore
import com.mpholo.project.grocery.android.data.model.CopyMonthRequest
import com.mpholo.project.grocery.android.data.model.CreateGroceryItemRequest
import com.mpholo.project.grocery.android.data.model.CreateProductRequest
import com.mpholo.project.grocery.android.data.model.GroceryItem
import com.mpholo.project.grocery.android.data.model.GroceryItemPatchRequest
import com.mpholo.project.grocery.android.data.model.MonthSummary
import com.mpholo.project.grocery.android.data.model.Product
import com.mpholo.project.grocery.android.data.model.UpdateBudgetRequest
import com.mpholo.project.grocery.android.data.sync.ConnectivityObserver

/**
 * Single point of access for the ViewModels. Reads fall back to the local cache
 * when offline; basket toggles work offline and are queued, then replayed to the
 * server by [syncPending] the moment the device reconnects.
 */
class GroceryRepository(
    private val api: GroceryApi = NetworkModule.groceryApi,
    private val store: LocalStore = AppGraph.store,
    private val connectivity: ConnectivityObserver = AppGraph.connectivity
) {

    /** Months: fresh from the server when reachable (and cached), else the cache. */
    suspend fun getMonths(): List<MonthSummary> = try {
        api.getMonths().also { store.saveMonths(it) }
    } catch (e: Exception) {
        store.loadMonths()
    }

    /**
     * Items of a month: fresh when reachable (and cached), else the cache; then
     * any pending offline toggles are overlaid so queued changes stay visible.
     */
    suspend fun getItems(monthlyGroceryId: Int): List<GroceryItem> {
        val base = try {
            api.getItems(monthlyGroceryId).also { store.saveItems(monthlyGroceryId, it) }
        } catch (e: Exception) {
            store.loadItems(monthlyGroceryId)
        }
        val pending = store.pendingMap()
        return if (pending.isEmpty()) base
        else base.map { it.copy(purchased = pending[it.groceryItemId] ?: it.purchased) }
    }

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

    /**
     * Toggle the "added to basket" (purchased) flag. Works offline: when the
     * device is offline (or the request fails) the change is queued locally and
     * an optimistic item is returned; it syncs on reconnect via [syncPending].
     */
    suspend fun setPurchased(item: GroceryItem, purchased: Boolean): GroceryItem {
        if (connectivity.isOnline()) {
            try {
                val updated = api.patchItem(
                    item.groceryItemId,
                    GroceryItemPatchRequest(purchased = purchased)
                )
                store.clearToggle(item.groceryItemId)
                return updated
            } catch (e: Exception) {
                // fall through to queue
            }
        }
        store.queueToggle(item.groceryItemId, purchased)
        return item.copy(purchased = purchased)
    }

    /** Replay queued offline basket toggles to the server. No-op when offline. */
    suspend fun syncPending() {
        if (!connectivity.isOnline()) return
        for (op in store.pendingToggles()) {
            try {
                api.patchItem(op.groceryItemId, GroceryItemPatchRequest(purchased = op.purchased))
                store.clearToggle(op.groceryItemId)
            } catch (e: Exception) {
                // keep it queued for the next reconnect
            }
        }
    }

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
