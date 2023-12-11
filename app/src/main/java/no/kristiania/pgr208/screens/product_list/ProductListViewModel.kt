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
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()
    
    init {
        loadProducts()
    }
    fun refreshProducts(){
        loadProducts()
    }
    private fun loadProducts() {
        // Async functions (Repository, data source) can ONLY be called within a coroutine-scope.
        // All ViewModels contains their own scope for this exact purpose, so we use it to call
        // our repository functions here.
        // As the functions are asynchronous, we can update our 'loading'-state inside the
        // coroutine-scope before calling the function and after receiving the receiving the response.
        // We then use this 'loading'-state to display a progress-indicator in our UI.
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            _products.value = ProductRepository.getProducts()
            _loading.value = false
        }
    }
}