package com.dicoding.wearit.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteItemDao {
    @Query("SELECT * FROM images")
    fun getAllFavoriteItems(): LiveData<List<Image>>

    @Query("SELECT COUNT(*) FROM images WHERE id = :itemId")
    fun isItemFavorite(itemId: Long): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteItem(item: Image)

    @Delete
    suspend fun deleteFavoriteItem(item: Image)
}
