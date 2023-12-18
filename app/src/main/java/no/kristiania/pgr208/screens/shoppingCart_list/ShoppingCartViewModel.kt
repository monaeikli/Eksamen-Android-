package no.kristiania.pgr208.screens.shoppingCart_list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.kristiania.pgr208.data.Product
import no.kristiania.pgr208.data.ProductRepository
import no.kristiania.pgr208.data.room.ShoppingCart

class ShoppingCartViewModel : ViewModel() {
    // Private MutableStateFlow to hold the list of products in the shopping cart
    private val _buyingProduct = MutableStateFlow<List<Product>>(emptyList())

    // Public StateFlow for external access to the list of products in the shopping cart
    val buyingProduct = _buyingProduct.asStateFlow()

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice = _totalPrice.asStateFlow()

    // Loads the products in the shopping cart by calling the corresponding repository function.
    // Updates the _buyingProduct StateFlow with the result.
    fun loadCart() {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfItemsInCartIds = ProductRepository.getShoppingCart().map { it.productId }
            _buyingProduct.value = ProductRepository.getProductsByIds(listOfItemsInCartIds)

            // Update total price after loading the cart
            updateTotalPrice()
        }
    }


    // Calculating and returns the total price of the cart
    fun getTotalPrice(): Int {
        return totalPrice.value
    }

    private fun updateTotalPrice() {
        val total = _buyingProduct.value.sumOf { it.price }
        _totalPrice.value = total
    }

}
