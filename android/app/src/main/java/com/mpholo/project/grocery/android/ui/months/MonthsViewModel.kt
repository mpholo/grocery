package com.mpholo.project.grocery.android.ui.months

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mpholo.project.grocery.android.data.GroceryRepository
import com.mpholo.project.grocery.android.data.model.MonthSummary
import com.mpholo.project.grocery.android.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MonthsViewModel(
    private val repository: GroceryRepository = GroceryRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<MonthSummary>>>(UiState.Loading)
    val state: StateFlow<UiState<List<MonthSummary>>> = _state.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        load()
    }

    /** Delete a month; the backend refuses (and we report) if its basket isn't empty. */
    fun deleteMonth(month: MonthSummary) {
        viewModelScope.launch {
            try {
                repository.deleteMonth(month.monthlyGroceryId)
                _message.value = "Deleted ${month.period ?: "month"}"
                load()
            } catch (e: Exception) {
                _message.value = e.message ?: "Could not delete month"
            }
        }
    }

    fun consumeMessage() {
        _message.value = null
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                _state.value = UiState.Success(repository.getMonths())
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Failed to load months")
            }
        }
    }
}
