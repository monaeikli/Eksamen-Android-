package no.kristiania.pgr208.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShoppingCartDao {
    @Query("SELECT * FROM ShoppingCart")
    suspend fun getShoppingCart(): List<ShoppingCart>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductToCart(shoppingCart: ShoppingCart)

    @Delete
    suspend fun removeFromCart(shoppingCart: ShoppingCart)
}