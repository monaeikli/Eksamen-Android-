package no.kristiania.pgr208.screens.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.kristiania.pgr208.data.Product
import no.kristiania.pgr208.data.ProductRepository
import no.kristiania.pgr208.data.room.Favorite

class ProductDetailsViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    fun setSelectedProduct(productId: Int) {
        // Async functions (Repository, data source) can ONLY be called within a coroutine-scope.
        // All ViewModels contains their own scope for this exact purpose, so we use it to call
        // our repository functions here.
        // As the functions are asynchronous, we can update our 'loading'-state inside the
        // coroutine-scope before calling the function and after receiving the receiving the response.
        // We then use this 'loading'-state to display a progress-indicator in our UI.
        viewModelScope.launch {
            _loading.value = true
            _selectedProduct.value = ProductRepository.getProductById(productId)
            _isFavorite.value = isCurrentProductFavorite()
            _loading.value = false
        }
    }


    fun updateFavorite(productId: Int) {
        viewModelScope.launch {
            if (isFavorite.value) {
                ProductRepository.removeFavorite(Favorite(productId))
            } else {
                ProductRepository.addFavorite(Favorite(productId))
            }
            _isFavorite.value = isCurrentProductFavorite()
        }
    }

    private suspend fun isCurrentProductFavorite(): Boolean {
       return ProductRepository.getFavorites().any { it.productId == selectedProduct.value?.id }
    }

}