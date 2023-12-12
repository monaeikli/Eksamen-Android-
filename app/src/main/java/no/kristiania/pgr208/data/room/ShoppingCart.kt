package no.kristiania.pgr208.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ShoppingCart")
data class ShoppingCart(
    @PrimaryKey
    val cartId: Int,
    val productId: Int,
    val productName: String,
    val totalPrice: Int
)