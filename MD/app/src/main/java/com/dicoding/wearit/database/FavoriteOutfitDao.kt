package com.dicoding.wearit.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteOutfitDao {
    @Insert
    suspend fun insertFavoriteOutfit(favoriteOutfit: FavoriteOutfit): Long

    @Delete
    suspend fun deleteFavoriteOutfit(favoriteOutfit: FavoriteOutfit)

    @Query("SELECT * FROM favorite_outfits")
    suspend fun getAllFavoriteOutfits(): List<FavoriteOutfit>
}
