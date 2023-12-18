package no.kristiania.pgr208.data

import android.media.Rating
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import no.kristiania.pgr208.data.room.ShoppingCart

//creating our product-table for the database that uses data from an API
@Entity
data class Product(
    @PrimaryKey
    val id: Int,
    @SerializedName("title")
    val name: String,
    val price: Int,
    val description: String,
    val category: String,
    @SerializedName("thumbnail")
    val productImage: String,
    val rating: Float,
)

//Adding this to display the products as an array, not an object
data class ProductListResponse(
    val products: List<Product>
)

