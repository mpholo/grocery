package com.mpholo.project.grocery.android.ui.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mpholo.project.grocery.android.data.GroceryRepository
import com.mpholo.project.grocery.android.data.model.GroceryItem
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
    val updatingBudget: Boolean = false // a budget update is in flight
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

    /** Toggle the "added to basket" flag for an item. */
    fun togglePurchased(item: GroceryItem) {
        patch(item.groceryItemId) {
            repository.setPurchased(item.groceryItemId, !item.purchased)
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
