package no.kristiania.pgr208.screens.product_list

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
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import no.kristiania.pgr208.screens.common.ProductItem



@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductClick: (productId: Int) -> Unit = {},
    navigateToFavoriteList: () -> Unit = {},
    navigateToShoppingCart: () -> Unit = {}
) {
    val loading = viewModel.loading.collectAsState()
    val products = viewModel.products.collectAsState()
    val scrollState = rememberScrollState()

    // Shows a loading circle while it's getting the data
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
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier.padding(30.dp),
            text = "Products",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,

        ) {
            IconButton(
                onClick = { viewModel.refreshProducts() }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh products"
                )
            }
            IconButton(
                onClick = { navigateToFavoriteList() }
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.Red
                )
            }
            IconButton(
                onClick = { navigateToShoppingCart() }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shopping cart",
                    tint = Color.DarkGray
                )
            }
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Orderhistory",
                tint = Color.DarkGray
            )
        }

        Divider()

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
            }
        }
    }
}
