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
//By making our MutableStateFlow-variable private and then introducing a separate
//public variable (backing property), we allow anyone to access (read) the ViewModel’s data,
//but only the ViewModel can change/update (write) the data
    private val _favoriteProduct = MutableStateFlow<List<Product>>(emptyList())

    val favoriteProduct = _favoriteProduct.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfFavoriteIds = ProductRepository.getFavorites().map { it.productId }
            _favoriteProduct.value = ProductRepository.getProductsByIds(listOfFavoriteIds)
        }
    }
}