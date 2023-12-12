package no.kristiania.pgr208.data

import no.kristiania.pgr208.data.room.ProductTypeConverter
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity
data class Product(
    @PrimaryKey
    val id: Int,
    @SerializedName("title")
    val name: String,
    val price: Double,
    val description: String,
    val category: String,
    @SerializedName("thumbnail")
    val productImage: String,
)

data class ProductListResponse(
    val products: List<Product>
)
