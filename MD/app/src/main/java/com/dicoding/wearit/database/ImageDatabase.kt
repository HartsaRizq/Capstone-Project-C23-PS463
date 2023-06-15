package com.dicoding.wearit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Image::class, FavoriteOutfit::class], version = 2)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun favoriteOutfitDao(): FavoriteOutfitDao

    companion object {
        private const val DATABASE_NAME = "images_database"

        @Volatile
        private var instance: ImagesDatabase? = null

        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE favorite_outfits ADD COLUMN new_column_name TEXT DEFAULT 'default_value' NOT NULL")
            }
        }

        fun getInstance(context: Context): ImagesDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ImagesDatabase::class.java,
                    DATABASE_NAME
                ).addMigrations(migration1to2)
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}
