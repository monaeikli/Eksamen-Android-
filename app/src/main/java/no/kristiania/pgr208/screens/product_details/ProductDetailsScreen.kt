package no.kristiania.pgr208.screens.product_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import coil.compose.AsyncImage
import no.kristiania.pgr208.theme.TransparentBlack

@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel,
    onBackButtonClick: () -> Unit = {}
) {
    val loading = viewModel.loading.collectAsState()
    val productState = viewModel.selectedProduct.collectAsState()
    val isFavorite = viewModel.isFavorite.collectAsState()

    if (loading.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // We 'abstract' the state value to a local variable here, so that we can reuse it
    // later in this function without having to check if it is NULL or not EVERYWHERE
    val product = productState.value
    if (product == null) {
        Text(text = "Failed to get product details. Selected product-object is NULL!")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBackButtonClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Refresh products"
                )
            }
            Text(
                modifier = Modifier.padding(30.dp),
                text = "Product Details",
                style = MaterialTheme.typography.titleLarge
            )

            IconButton(
                onClick = { viewModel.updateFavorite(product.id) }
            ) {
                Icon(
                    imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Update Favorite",
                    tint = Color.Red
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp)
                .verticalScroll(state = rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray),
                model = product.productImage,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                contentDescription = "Image of ${product.name}"
            )
            Spacer(modifier = Modifier.height(39.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = product.category,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.description,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Price: " + product.price.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis
            )
            Button(onClick = { addToShoppingCart() }) {
                Text(text = "Add to cart")
            }



        }
    }
}