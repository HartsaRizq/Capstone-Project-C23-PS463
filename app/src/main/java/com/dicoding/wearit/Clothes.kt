package com.dicoding.wearit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clothes(
    val photo: Int
): Parcelable
