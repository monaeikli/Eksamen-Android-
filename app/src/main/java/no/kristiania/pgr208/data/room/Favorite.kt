package no.kristiania.pgr208.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductFavorite")
data class Favorite(
    @PrimaryKey
    val productId: Int
)