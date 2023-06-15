package com.dicoding.wearit.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.wearit.database.FavoriteOutfit
import com.dicoding.wearit.R
import com.dicoding.wearit.database.FavoriteOutfitDao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteAdapter(private val favoriteOutfitDao: FavoriteOutfitDao) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var favoriteOutfits: MutableList<FavoriteOutfit> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_image_card, parent, false)
        return FavoriteViewHolder(itemView, favoriteOutfitDao)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteOutfit = favoriteOutfits[position]
        holder.bind(favoriteOutfit, this)
    }

    override fun getItemCount(): Int {
        return favoriteOutfits.size
    }

    fun setData(favoriteOutfits: List<FavoriteOutfit>) {
        this.favoriteOutfits.clear()
        this.favoriteOutfits.addAll(favoriteOutfits)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        favoriteOutfits.removeAt(position)
        notifyItemRemoved(position)
    }

    class FavoriteViewHolder(itemView: View, private val favoriteOutfitDao: FavoriteOutfitDao) : RecyclerView.ViewHolder(itemView) {
        private val fab: FloatingActionButton = itemView.findViewById(R.id.favoriteButton)
        private val imageView1: ImageView = itemView.findViewById(R.id.iv1)
        private val imageView2: ImageView = itemView.findViewById(R.id.iv2)
        private val imageView3: ImageView = itemView.findViewById(R.id.iv3)
        private val labelTextView1: TextView = itemView.findViewById(R.id.tvCategory1)
        private val labelTextView2: TextView = itemView.findViewById(R.id.tvCategory2)
        private val labelTextView3: TextView = itemView.findViewById(R.id.tvCategory3)
        fun bind(favoriteOutfit: FavoriteOutfit, favoriteAdapter: FavoriteAdapter) {
            itemView.apply {
                Glide.with(context)
                    .load(favoriteOutfit.outerImagePath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView1)

                Glide.with(context)
                    .load(favoriteOutfit.topImagePath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView2)

                Glide.with(context)
                    .load(favoriteOutfit.bottomImagePath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView3)

                labelTextView1.text = favoriteOutfit.outerLabel
                labelTextView2.text = favoriteOutfit.topLabel
                labelTextView3.text = favoriteOutfit.bottomLabel

                fab.setImageResource(R.drawable.ic_baseline_favorite_full_white_24px)

                fab.setOnClickListener {
                    removeOutfitFromDatabase(favoriteOutfit)
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        favoriteAdapter.removeItem(position)
                    }
                }
            }

        }

        private fun removeOutfitFromDatabase(favoriteOutfit: FavoriteOutfit) {
            CoroutineScope(Dispatchers.IO).launch {
                favoriteOutfitDao.deleteFavoriteOutfit(favoriteOutfit)
            }
        }
    }
}