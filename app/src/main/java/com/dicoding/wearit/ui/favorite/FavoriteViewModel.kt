package com.dicoding.wearit.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.wearit.Database.FavoriteDatabase
import com.dicoding.wearit.Database.FavoriteItemDao
import com.dicoding.wearit.Database.Image
import com.dicoding.wearit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteItemDao: FavoriteItemDao
    val favoriteItems: LiveData<List<Image>>

    init {
        val database = FavoriteDatabase.getDatabase(application)
        favoriteItemDao = database.favoriteItemDao()
        favoriteItems = favoriteItemDao.getAllFavoriteItems()
    }

    fun insertFavoriteItem(item: Image) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteItemDao.insertFavoriteItem(item)
        }
    }

    fun deleteFavoriteItem(item: Image) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteItemDao.deleteFavoriteItem(item)
        }
    }

    fun isItemFavorite(item: Image): Boolean {
        return favoriteItemDao.isItemFavorite(item.id) > 0
    }
}
