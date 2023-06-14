package com.dicoding.wearit.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.wearit.Database.Image
import com.dicoding.wearit.databinding.ItemFavoriteBinding

data class FavoriteItem(val name: String, val imageResId: Int)

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var favoriteItems: List<Image> = listOf()
    private var onFavoriteClickListener: ((Image) -> Unit)? = null

    fun setItems(items: List<Image>) {
        favoriteItems = items
        notifyDataSetChanged()
    }

    fun setOnFavoriteClickListener(listener: (Image) -> Unit) {
        onFavoriteClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFavoriteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = favoriteItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return favoriteItems.size
    }

    inner class ViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.favoriteItemLoveIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = favoriteItems[position]
                    onFavoriteClickListener?.invoke(item)
                }
            }
        }

        fun bind(item: Image) {
//            binding.itemName.text = item.predictedLabel
//            binding.itemColor.text = item.color
        }
    }
}
