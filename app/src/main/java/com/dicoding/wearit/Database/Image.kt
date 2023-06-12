package com.dicoding.wearit.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "predicted_label") val predictedLabel: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "image_path") val imagePath: String
)
