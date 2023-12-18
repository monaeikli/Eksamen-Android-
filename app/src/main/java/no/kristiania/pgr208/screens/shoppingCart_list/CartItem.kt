package no.kristiania.pgr208.screens.shoppingCart_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import no.kristiania.pgr208.data.Product
import no.kristiania.pgr208.data.ProductRepository
import no.kristiania.pgr208.data.room.ShoppingCart


@Composable
fun CartItem(
    product: Product,
    onClick: () -> Unit = {},
    viewModel: ShoppingCartViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 6.dp
            )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(5)
            )
            .background(color = Color.White)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the image
        AsyncImage(
            modifier = Modifier
                .size(108.dp, 108.dp)
                .background(color = Color.Gray),
            model = product.productImage,
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            contentDescription = "Image of ${product.name}"
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Product name
            Text(
                text = product.name,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // Product Price
            Text(
                text = "$${product.price}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Check if cartIdToRemove is not null before attempting to remove from cart
        IconButton(onClick = {
            viewModel.viewModelScope.launch {
                val cartIdToRemove = ProductRepository.getCartIdByProductId(product.id)
                cartIdToRemove?.let {
                    ProductRepository.removeFromCart(ShoppingCart(it, 0, "", 0))
                }
                // Remove the item from the cart using the retrieved cart ID
                ProductRepository.getCartIdByProductId(product.id)?.let { cartIdToRemove ->
                    ProductRepository.removeFromCart(ShoppingCart(cartIdToRemove, 0, "", 0))
                }

                // Reload the cart and update the total price
                viewModel.loadCart()
            }
        }) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "remove item"
            )
        }
    }
}