package no.kristiania.pgr208.screens.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.kristiania.pgr208.data.Product
import no.kristiania.pgr208.data.ProductRepository

class ProductListViewModel : ViewModel() {
    // A MutableStateFlow to represent the loading state
    private val _loading = MutableStateFlow(false)
    // Exposing the loading state as an immutable StateFlow
    val loading = _loading.asStateFlow()

    // A MutableStateFlow to hold the list of products
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    // Exposing the products list as an immutable StateFlow
    val products = _products.asStateFlow()

    // Initializing the ViewModel by loading products
    init {
        loadProducts()
    }

    // Function to refresh the list of products
    fun refreshProducts(){
        loadProducts()
    }

    // Private function to load products from the repository
    private fun loadProducts() {
        // As the functions are asynchronous, we update our 'loading'-state inside the coroutine-scope
        viewModelScope.launch(Dispatchers.IO) {
            // Setting loading to true before fetching the products
            _loading.value = true
            // Fetching the list of products from the repository
            _products.value = ProductRepository.getProducts()
            // Setting loading to false after receiving the response
            _loading.value = false
        }
    }
    fun getCategories(): List<String> {
        return _products.value.map { it.category }.distinct()
    }
}