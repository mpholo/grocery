package com.mpholo.project.grocery.android.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mpholo.project.grocery.android.data.GroceryRepository
import com.mpholo.project.grocery.android.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductsScreenState(
    val loading: Boolean = true,
    val products: List<Product> = emptyList(),
    val error: String? = null,     // fatal load error
    val message: String? = null,   // transient snackbar feedback
    val adding: Boolean = false
)

class ProductsViewModel(
    private val repository: GroceryRepository = GroceryRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ProductsScreenState())
    val state: StateFlow<ProductsScreenState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val products = repository.getProducts().sortedBy { it.productName.lowercase() }
                _state.update { it.copy(loading = false, products = products, error = null) }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message ?: "Failed to load products") }
            }
        }
    }

    fun addProduct(name: String, description: String?, price: Double) {
        _state.update { it.copy(adding = true) }
        viewModelScope.launch {
            try {
                repository.addProduct(name.trim(), description?.trim()?.ifBlank { null }, price)
                _state.update { it.copy(adding = false, message = "Added \"$name\"") }
                load()
            } catch (e: Exception) {
                _state.update { it.copy(adding = false, message = e.message ?: "Could not add product") }
            }
        }
    }

    fun editProduct(product: Product, name: String, description: String?, price: Double) {
        val id = product.productId ?: return
        _state.update { it.copy(adding = true) }
        viewModelScope.launch {
            try {
                repository.updateProduct(id, name.trim(), description?.trim()?.ifBlank { null }, price)
                _state.update { it.copy(adding = false, message = "Updated \"$name\"") }
                load()
            } catch (e: Exception) {
                _state.update { it.copy(adding = false, message = e.message ?: "Could not update product") }
            }
        }
    }

    fun deleteProduct(product: Product) {
        val id = product.productId ?: return
        viewModelScope.launch {
            try {
                repository.deleteProduct(id)
                _state.update { it.copy(message = "Deleted \"${product.productName}\"") }
                load()
            } catch (e: Exception) {
                _state.update { it.copy(message = e.message ?: "Could not delete product") }
            }
        }
    }

    fun consumeMessage() = _state.update { it.copy(message = null) }
}
