package no.kristiania.pgr208.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

//creating our shoppingcart-table for the database
@Entity(tableName = "ShoppingCart")
data class ShoppingCart(
    @PrimaryKey(autoGenerate = true)
    val cartId: Int,
    val productId: Int,
    val productName: String,
    val productPrice: Int,
)
