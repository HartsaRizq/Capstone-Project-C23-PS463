package com.dicoding.wearit.database

import java.util.UUID

data class Outfit(
    val outer: Image,
    val top: Image,
    val bottom: Image,
    var favoriteOutfitId: Long = System.currentTimeMillis(),
    var isFavorite: Boolean = false
)
