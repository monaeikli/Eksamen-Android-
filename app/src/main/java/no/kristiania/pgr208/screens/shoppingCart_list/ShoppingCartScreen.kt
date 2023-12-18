package no.kristiania.pgr208.screens.shoppingCart_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.kristiania.pgr208.data.room.ShoppingCart
import no.kristiania.pgr208.screens.shoppingCart_list.ShoppingCartViewModel



@Composable
fun ShoppingCartScreen(
    viewModel: ShoppingCartViewModel,
    onBackButtonClick: () -> Unit = {},
    onOrderClick: () -> Unit = {},
    onProductClick: (productId: Int) -> Unit = {},
) {
    // Collecting the list of products in the shopping cart and the total price from the ViewModel
    val products = viewModel.buyingProduct.collectAsState()
    val totalPrice = viewModel.getTotalPrice()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button in the top bar
            IconButton(
                onClick = { onBackButtonClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }

            // Title of the screen
            Text(
                modifier = Modifier.padding(20.dp),
                text = "ShoppingCart",
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Divider between top bar and product list
        Divider()

        // Product List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .weight(1f),
        ) {
            items(products.value) { product ->
                // Displaying each item in the shopping cart
                CartItem(
                    product = product,
                    onClick = {
                        onProductClick(product.id)
                    },
                    viewModel = viewModel
                )

                // Divider between each item
                Divider()
            }
        }

        // Total price section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Displaying the total price
            Text(
                text = "Total:",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "$$totalPrice",
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Place order button
        Button(
            onClick = { onOrderClick() },
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(1.dp)
                .weight(0.1f)
        ) {
            Text("Place order")
        }
    }
}

