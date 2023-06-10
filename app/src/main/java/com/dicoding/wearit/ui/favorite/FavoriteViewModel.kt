package com.dicoding.wearit.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.wearit.R
class FavoriteViewModel : ViewModel() {

    private val _favoriteItems = MutableLiveData<List<FavoriteItem>>()
    val favoriteItems: LiveData<List<FavoriteItem>> = _favoriteItems

    init {
        val items = listOf(
            FavoriteItem("Item 1", R.drawable.item1),
            FavoriteItem("Item 2", R.drawable.item2),
            FavoriteItem("Item 3", R.drawable.item3),
            FavoriteItem("Item 4", R.drawable.item4),
            FavoriteItem("Item 5", R.drawable.item5)
        )
        _favoriteItems.value = items
    }
}
