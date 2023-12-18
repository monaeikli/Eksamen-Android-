package no.kristiania.pgr208.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShoppingCartDao {
    // Retrieving all items from the "ShoppingCart" table
    @Query("SELECT * FROM ShoppingCart")
    suspend fun getShoppingCart(): List<ShoppingCart>

    // Retrieving the cart ID for a specific product
    // from the "ShoppingCart" table
    @Query("SELECT cartId FROM ShoppingCart WHERE productId = :productId LIMIT 1")
    suspend fun getCartIdByProductId(productId: Int): Int?

    // Insert a product into the "ShoppingCart" table
    // If a conflict occurs, replace the existing entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductToCart(shoppingCart: ShoppingCart)

    // Delete a product from the "ShoppingCart" table
    @Delete
    suspend fun removeFromCart(shoppingCart: ShoppingCart)
}