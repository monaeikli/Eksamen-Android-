package no.kristiania.pgr208.screens.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.kristiania.pgr208.data.Product
import no.kristiania.pgr208.data.ProductRepository
import no.kristiania.pgr208.data.room.Favorite
import no.kristiania.pgr208.data.room.ShoppingCart

class ProductDetailsViewModel : ViewModel() {
    // Loading state for indicating data retrieval progress
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    // State flow for holding the selected product details
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    // State flow for tracking whether the selected product is a favorite
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    // State flow for tracking whether the selected product is in the cart
    private val _isInCart = MutableStateFlow(false)
    val isInCart = _isInCart.asStateFlow()

    // Function to set the selected product and update related states
    fun setSelectedProduct(productId: Int) {
        // Async functions can only be called within a coroutine-scope.
        // Therefore we will use viewModelScope.launch to set
        // the selected product and update their related states
        viewModelScope.launch {
            _loading.value = true
            _selectedProduct.value = ProductRepository.getProductById(productId)
            _isFavorite.value = isCurrentProductFavorite()
            _isInCart.value = isCurrentlyInCart()
            _loading.value = false
        }
    }

    // Function to update the favorite status of the selected product
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

    // Function to update the cart status of the selected product
    fun updateCart(cartId: Int) {
        viewModelScope.launch {
            val selectedProduct = selectedProduct.value
            if (isInCart.value) {
                ProductRepository.removeFromCart(ShoppingCart(cartId, 0, "", 0))
            } else {
                selectedProduct?.let {
                    ProductRepository.addToShoppingCart(
                        ShoppingCart(
                            cartId = it.id,
                            productId = it.id,
                            productName = it.name,
                            productPrice = it.price
                        )
                    )
                }
            }
            _isInCart.value = isCurrentlyInCart()
        }
    }
    private suspend fun isCurrentProductFavorite(): Boolean {
        return ProductRepository.getFavorites().any { it.productId == selectedProduct.value?.id }
    }

    private suspend fun isCurrentlyInCart(): Boolean {
        return ProductRepository.getShoppingCart().any { it.productId == selectedProduct.value?.id }
    }
}