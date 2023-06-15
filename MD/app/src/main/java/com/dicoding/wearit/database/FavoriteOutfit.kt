package com.dicoding.wearit.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_outfits")
data class FavoriteOutfit (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "outer_label") val outerLabel: String,
    @ColumnInfo(name = "outer_image_path") val outerImagePath: String,
    @ColumnInfo(name = "top_label") val topLabel: String,
    @ColumnInfo(name = "top_image_path") val topImagePath: String,
    @ColumnInfo(name = "bottom_label") val bottomLabel: String,
    @ColumnInfo(name = "bottom_image_path") val bottomImagePath: String,
)