package no.kristiania.pgr208.screens.product_details

import android.support.v4.os.IResultReceiver2.Default
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel,
    onBackButtonClick: () -> Unit = {}
) {
    // Collecting state variables from the ViewModel
    val loading = viewModel.loading.collectAsState()
    val productState = viewModel.selectedProduct.collectAsState()
    val isFavorite = viewModel.isFavorite.collectAsState()

    // Local state to track whether the product is added to the cart
    var addedToCart by rememberSaveable { mutableStateOf(false) }

    // Display a loading indicator if the data is still loading
    if (loading.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Retrieve the product from the collected state
    val product = productState.value

    // Display an error message if the product object is null
    if (product == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Failed to get product details. Selected product-object is NULL!",
                color = Color.Black
            )
        }
        return
    }

    // Product Details Screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Back button
            IconButton(
                onClick = { onBackButtonClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back"
                )
            }

            // Screen title
            Text(
                modifier = Modifier.weight(1f),
                text = "Product Details",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            // Favorite button
            IconButton(
                onClick = { viewModel.updateFavorite(product.id) }
            ) {
                Icon(
                    imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.Red
                )
            }
        }


        // Product Details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(top = 10.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = product.productImage,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Image of ${product.name}"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Product Name
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row {
                // Product Rating
                Icon(imageVector = Icons.Default.Star, contentDescription = "Rating")
                Text(
                    text = product.rating.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Product Category
            Text(
                text = product.category,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Product Description
            Text(
                text = product.description,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Product Price
            Text(
                text = "Price: $${product.price}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Add to Cart Button
            Button(
                onClick = {
                    viewModel.updateCart(product.id)
                    addedToCart = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !addedToCart
            ) {
                Text(text = if (addedToCart) "Added to Cart" else "Add to Cart")
            }
        }
    }
}
