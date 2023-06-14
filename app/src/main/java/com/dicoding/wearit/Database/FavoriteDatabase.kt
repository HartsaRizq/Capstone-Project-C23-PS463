package com.dicoding.wearit.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.wearit.ui.favorite.FavoriteItem

@Database(entities = [FavoriteItem::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteItemDao(): FavoriteItemDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteDatabase? = null

        fun getDatabase(context: Context): FavoriteDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDatabase::class.java,
                    "favorite_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
