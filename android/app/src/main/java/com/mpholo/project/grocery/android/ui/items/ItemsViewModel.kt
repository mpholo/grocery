package com.mpholo.project.grocery.android.ui.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mpholo.project.grocery.android.data.GroceryRepository
import com.mpholo.project.grocery.android.data.model.GroceryItem
import com.mpholo.project.grocery.android.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ItemsScreenState(
    val loading: Boolean = true,
    val items: List<GroceryItem> = emptyList(),
    val error: String? = null,          // fatal load error (blocks the screen)
    val message: String? = null,        // transient feedback for a snackbar
    val updatingIds: Set<Int> = emptySet(),
    val copying: Boolean = false,       // a copy-to-month request is in flight
    val budget: Double = 0.0,           // editable month budget
    val updatingBudget: Boolean = false, // a budget update is in flight
    val products: List<Product> = emptyList(), // catalog, for the add-item picker
    val loadingProducts: Boolean = false,
    val addingItem: Boolean = false     // an add-product-to-month request is in flight
) {
    /** Total of every item in the month (regardless of basket state). */
    val plannedTotal: Double get() = items.sumOf { it.actualPrice * it.quantity }

    /** Total of items currently ticked as added to basket — moves as you toggle. */
    val basketTotal: Double get() = items.filter { it.purchased }.sumOf { it.actualPrice * it.quantity }

    val purchasedCount: Int get() = items.count { it.purchased }
}

class ItemsViewModel(
    private val monthlyGroceryId: Int,
    initialBudget: Double,
    private val repository: GroceryRepository = GroceryRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ItemsScreenState(budget = initialBudget))
    val state: StateFlow<ItemsScreenState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val items = repository.getItems(monthlyGroceryId)
                _state.update { it.copy(loading = false, items = items, error = null) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(loading = false, error = e.message ?: "Failed to load items")
                }
            }
        }
    }

    /**
     * Toggle the "added to basket" flag. Optimistic: the item flips immediately
     * (so it animates to the other section right away), and we reconcile with the
     * server response in the background, reverting if the request fails.
     */
    fun togglePurchased(item: GroceryItem) {
        val id = item.groceryItemId
        val target = !item.purchased
        _state.update { current ->
            current.copy(items = current.items.map {
                if (it.groceryItemId == id) it.copy(purchased = target) else it
            })
        }
        viewModelScope.launch {
            try {
                val updated = repository.setPurchased(id, target)
                _state.update { current ->
                    current.copy(items = current.items.map {
                        if (it.groceryItemId == id) updated else it
                    })
                }
            } catch (e: Exception) {
                _state.update { current ->
                    current.copy(
                        items = current.items.map {
                            if (it.groceryItemId == id) it.copy(purchased = !target) else it
                        },
                        message = e.message ?: "Update failed"
                    )
                }
            }
        }
    }

    /** Change quantity and/or unit price for an item. */
    fun updatePriceAndQuantity(item: GroceryItem, quantity: Int, actualPrice: Double) {
        patch(item.groceryItemId) {
            repository.updateItem(
                groceryItemId = item.groceryItemId,
                quantity = quantity,
                actualPrice = actualPrice
            )
        }
    }

    /** Copy this month (with its items) into the chosen month/year; items reset to not-in-basket. */
    fun copyToMonth(month: Int, year: Int) {
        _state.update { it.copy(copying = true) }
        viewModelScope.launch {
            try {
                val created = repository.copyMonth(monthlyGroceryId, month, year)
                _state.update { it.copy(copying = false, message = "Copied to ${created.period}") }
            } catch (e: Exception) {
                _state.update { it.copy(copying = false, message = e.message ?: "Copy failed") }
            }
        }
    }

    /** Fetch the product catalog for the add-item picker (lazy, when the dialog opens). */
    fun loadProducts() {
        if (state.value.products.isNotEmpty() || state.value.loadingProducts) return
        _state.update { it.copy(loadingProducts = true) }
        viewModelScope.launch {
            try {
                val products = repository.getProducts()
                _state.update { it.copy(loadingProducts = false, products = products) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(loadingProducts = false, message = e.message ?: "Failed to load products")
                }
            }
        }
    }

    /** Add a catalog product to this month (quantity 1, at the product's price). */
    fun addItem(product: Product) {
        val productId = product.productId ?: return
        _state.update { it.copy(addingItem = true) }
        viewModelScope.launch {
            try {
                val created = repository.addItemToMonth(
                    monthlyGroceryId = monthlyGroceryId,
                    productId = productId,
                    quantity = 1,
                    actualPrice = product.productPrice
                )
                _state.update { current ->
                    current.copy(
                        addingItem = false,
                        items = current.items + created,
                        message = "Added ${product.productName}"
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(addingItem = false, message = e.message ?: "Add failed") }
            }
        }
    }

    /** Remove an item from this month. */
    fun deleteItem(item: GroceryItem) {
        val id = item.groceryItemId
        _state.update { it.copy(updatingIds = it.updatingIds + id) }
        viewModelScope.launch {
            try {
                repository.deleteItem(id)
                _state.update { current ->
                    current.copy(
                        items = current.items.filterNot { it.groceryItemId == id },
                        updatingIds = current.updatingIds - id,
                        message = "Removed ${item.productName ?: "item"}"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(updatingIds = it.updatingIds - id, message = e.message ?: "Delete failed")
                }
            }
        }
    }

    /** Update the month's budget amount. */
    fun updateBudget(newBudget: Double) {
        _state.update { it.copy(updatingBudget = true) }
        viewModelScope.launch {
            try {
                val updated = repository.updateBudget(monthlyGroceryId, newBudget)
                _state.update {
                    it.copy(
                        updatingBudget = false,
                        budget = updated.budgetAmount,
                        message = "Budget updated"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(updatingBudget = false, message = e.message ?: "Budget update failed")
                }
            }
        }
    }

    fun consumeMessage() = _state.update { it.copy(message = null) }

    private fun patch(itemId: Int, block: suspend () -> GroceryItem) {
        _state.update { it.copy(updatingIds = it.updatingIds + itemId) }
        viewModelScope.launch {
            try {
                val updated = block()
                _state.update { current ->
                    current.copy(
                        items = current.items.map { if (it.groceryItemId == itemId) updated else it },
                        updatingIds = current.updatingIds - itemId
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        updatingIds = it.updatingIds - itemId,
                        message = e.message ?: "Update failed"
                    )
                }
            }
        }
    }

    companion object {
        fun provideFactory(monthlyGroceryId: Int, initialBudget: Double): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ItemsViewModel(monthlyGroceryId, initialBudget) as T
            }
    }
}
