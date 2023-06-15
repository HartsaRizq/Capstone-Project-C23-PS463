package com.dicoding.wearit.ui.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.wearit.database.FavoriteOutfit
import com.dicoding.wearit.database.FavoriteOutfitDao
import com.dicoding.wearit.database.Outfit
import com.dicoding.wearit.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var outfitList: List<Outfit> = emptyList()
    private lateinit var favoriteOutfitDao: FavoriteOutfitDao

    fun setData(outfits: List<Outfit>) {
        outfitList = outfits
        notifyDataSetChanged()

        CoroutineScope(Dispatchers.IO).launch {
            val favoriteOutfits = favoriteOutfitDao.getAllFavoriteOutfits()
            for (outfit in outfitList) {
                outfit.isFavorite = favoriteOutfits.any { favoriteOutfit ->
                    favoriteOutfit.outerImagePath == outfit.outer.imagePath &&
                            favoriteOutfit.topImagePath == outfit.top.imagePath &&
                            favoriteOutfit.bottomImagePath == outfit.bottom.imagePath
                }
            }
            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }
    }

    fun setFavoriteOutfitDao(favoriteOutfitDao: FavoriteOutfitDao) {
        this.favoriteOutfitDao = favoriteOutfitDao
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_image_card, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val outfit = outfitList[position]
        holder.bind(outfit)
    }

    override fun getItemCount(): Int {
        return outfitList.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fab: FloatingActionButton = itemView.findViewById(R.id.favoriteButton)
        private val imageView1: ImageView = itemView.findViewById(R.id.iv1)
        private val imageView2: ImageView = itemView.findViewById(R.id.iv2)
        private val imageView3: ImageView = itemView.findViewById(R.id.iv3)
        private val labelTextView1: TextView = itemView.findViewById(R.id.tvCategory1)
        private val labelTextView2: TextView = itemView.findViewById(R.id.tvCategory2)
        private val labelTextView3: TextView = itemView.findViewById(R.id.tvCategory3)


        fun bind(outfit: Outfit) {
            // Bind the data to the views
            Glide.with(itemView.context)
                .load(outfit.outer.imagePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView1)

            Glide.with(itemView.context)
                .load(outfit.top.imagePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView2)

            Glide.with(itemView.context)
                .load(outfit.bottom.imagePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView3)

            labelTextView1.text = outfit.outer.predictedLabel
            labelTextView2.text = outfit.top.predictedLabel
            labelTextView3.text = outfit.bottom.predictedLabel

            if (outfit.isFavorite) {
                fab.setImageResource(R.drawable.ic_baseline_favorite_full_white_24px)
            } else {
                fab.setImageResource(R.drawable.ic_baseline_favorite_border_white_24px)
            }

            fab.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedOutfit = outfit
                    if (clickedOutfit.isFavorite) {
                        removeFavoriteOutfit(clickedOutfit)
                    } else {
                        addFavoriteOutfit(clickedOutfit)
                    }
                }
            }
        }

        private fun addFavoriteOutfit(outfit: Outfit) {
            val favoriteOutfit = FavoriteOutfit(
                id = outfit.favoriteOutfitId,
                outerLabel = outfit.outer.predictedLabel,
                topLabel = outfit.top.predictedLabel,
                bottomLabel = outfit.bottom.predictedLabel,
                outerImagePath = outfit.outer.imagePath,
                topImagePath = outfit.top.imagePath,
                bottomImagePath = outfit.bottom.imagePath,
            )

            // Insert the favorite outfit details into the favorite_outfits table
            CoroutineScope(Dispatchers.IO).launch {
                favoriteOutfitDao.insertFavoriteOutfit(favoriteOutfit)
                outfit.isFavorite = true // Update the isFavorite field
                withContext(Dispatchers.Main) {
                    fab.setImageResource(R.drawable.ic_baseline_favorite_full_white_24px) // Update the fab button appearance
                }
            }
        }

        private fun removeFavoriteOutfit(outfit: Outfit) {
            val favoriteOutfit = FavoriteOutfit(
                id = outfit.favoriteOutfitId,
                outerLabel = outfit.outer.predictedLabel,
                topLabel = outfit.top.predictedLabel,
                bottomLabel = outfit.bottom.predictedLabel,
                outerImagePath = outfit.outer.imagePath,
                topImagePath = outfit.top.imagePath,
                bottomImagePath = outfit.bottom.imagePath,
            )

            // Remove the favorite outfit from the favorite_outfits table
            CoroutineScope(Dispatchers.IO).launch {
                favoriteOutfitDao.deleteFavoriteOutfit(favoriteOutfit)
                outfit.isFavorite = false // Update the isFavorite field
                withContext(Dispatchers.Main) {
                    fab.setImageResource(R.drawable.ic_baseline_favorite_border_white_24px)
                }
            }
        }


        init {
            itemView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        // Set the layout manager to horizontal orientation
        val layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.isNestedScrollingEnabled = false
    }
}