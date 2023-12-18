package no.kristiania.pgr208.screens.product_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import no.kristiania.pgr208.screens.common.ProductItem



@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductClick: (productId: Int) -> Unit = {},
    navigateToFavoriteList: () -> Unit = {},
    navigateToShoppingCart: () -> Unit = {},
    navigateToOrderHistory: () -> Unit = {},
) {
    // Collecting loading state and product list from the ViewModel
    val loading = viewModel.loading.collectAsState()
    val products = viewModel.products.collectAsState()

    // Remembering the scroll state for the LazyColumn
    val scrollState = rememberScrollState()

    // Shows a loading circle while it's retrieving the data
    if (loading.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        // Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Products",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Refresh button in the header
                IconButton(
                    onClick = { viewModel.refreshProducts() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh products"
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(bottom = 16.dp))

        // Product List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(products.value) { product ->
                ProductItem(
                    product = product,
                    onClick = {
                        onProductClick(product.id)
                    }
                )
        Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
        Row (
            modifier = Modifier
                .padding(bottom = 0.dp)
                .fillMaxWidth(1f)
                .background(color = Color.LightGray),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            // Navigate home
            IconButton(
                onClick = { /*Decor*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.Black
                )
            }

            //Navigate to favorites page
            IconButton(
                onClick = { navigateToFavoriteList() }
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.Red
                )
            }

            //Navigate to ShoppingCart page
            IconButton(
                onClick = { navigateToShoppingCart() }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shopping cart",
                    tint = Color.DarkGray
                )
            }

            //Navigate to Order History page
            IconButton(
                onClick = { navigateToOrderHistory() }
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Order history",
                    tint = Color.DarkGray
                )
            }
        }
    }
}
