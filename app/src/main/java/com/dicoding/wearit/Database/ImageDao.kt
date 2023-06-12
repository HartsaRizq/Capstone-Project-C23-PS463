package com.dicoding.wearit.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert
    suspend fun insertImage(image: Image): Long

    @Query("DELETE FROM images")
    suspend fun clearAllTables()
}
