package no.kristiania.pgr208.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import no.kristiania.pgr208.data.Product

@Dao
interface ProductDao {
    // Retrieving all products from the "Product" table
    @Query("SELECT * FROM Product")
    suspend fun getProducts(): List<Product>

    // Retrieving a product by its unique ID from the "Product" table
    @Query("SELECT * FROM Product WHERE id = :productId")
    suspend fun getProductById(productId: Int): Product?

    // Retrieving products by a list of IDs from the "Product" table
    @Query("SELECT * FROM Product WHERE id IN (:idList)")
    suspend fun getProductsByIds(idList: List<Int>): List<Product>

    @Query("SELECT * FROM PRODUCT WHERE Category IN (:categoryList)")
    suspend fun getProductsByCategory(categoryList: List<String>): List<Product>

    // Insert products into the "Product" table
    // If a conflict occurs (duplicate entry),
    // replace the existing entries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)
}
