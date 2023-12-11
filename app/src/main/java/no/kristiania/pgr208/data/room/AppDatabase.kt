package no.kristiania.pgr208.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import no.kristiania.pgr208.data.Product

@Database(
    entities = [Product::class, Favorite::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun favoriteDao(): FavoriteDao
}