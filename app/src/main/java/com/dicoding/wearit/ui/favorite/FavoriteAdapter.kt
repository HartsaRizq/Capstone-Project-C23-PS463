package com.dicoding.wearit.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.wearit.databinding.ItemFavoriteBinding

data class FavoriteItem(val name: String, val imageResId: Int)

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val items = mutableListOf<FavoriteItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<FavoriteItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFavoriteBinding.inflate(inflater, parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FavoriteItem) {
            binding.favoriteItemCaption.text = item.name
            binding.favoriteItemImage.setImageResource(item.imageResId)
        }
    }
}
