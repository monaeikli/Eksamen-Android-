package no.kristiania.pgr208.screens.favorite_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.kristiania.pgr208.data.Product
import no.kristiania.pgr208.data.ProductRepository

class FavoriteListViewModel : ViewModel() {
// By keeping the MutableStateFlow private and creating a public
// backing property, we enable external access (read) to the ViewModel's data,
// only the ViewModel can change or update the data
    private val _favoriteProduct = MutableStateFlow<List<Product>>(emptyList())

    val favoriteProduct = _favoriteProduct.asStateFlow()

    // Function to load the list of favorite products
    fun loadFavorites() {
        // Launch a coroutine in the IO dispatcher to ble
        // able to run the operation asynchronously
        viewModelScope.launch(Dispatchers.IO) {
            // Retrieving the list of favorite product IDs from the repository
            val listOfFavoriteIds = ProductRepository.getFavorites().map { it.productId }
            // Getting the products with the retrieved favorite IDs from the repository
            _favoriteProduct.value = ProductRepository.getProductsByIds(listOfFavoriteIds)
        }
    }
}