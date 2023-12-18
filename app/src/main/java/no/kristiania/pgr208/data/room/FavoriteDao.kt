package no.kristiania.pgr208.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    // Query to retrieve all favorites from the "ProductFavorite" table
    @Query("SELECT * FROM ProductFavorite")
    suspend fun getFavorites(): List<Favorite>

    // Insert a product into the "ProductFavorite" table
    // If a conflict occurs (duplicate entry),
    // this will replace the existing entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    // Delete a favorite from the "ProductFavorite" table
    @Delete
    suspend fun removeFavorite(favorite: Favorite)
}