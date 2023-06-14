package com.dicoding.wearit.ui.recommendation

// In RecommendationFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.wearit.Database.Image
import com.dicoding.wearit.Database.Outfit
import com.dicoding.wearit.R
class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var outfitList: List<Outfit> = emptyList()
    fun setData(outfits: List<Outfit>) {
        outfitList = outfits
        notifyDataSetChanged()
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

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView1: ImageView = itemView.findViewById(R.id.iv1)
        private val imageView2: ImageView = itemView.findViewById(R.id.iv2)
        private val imageView3: ImageView = itemView.findViewById(R.id.iv3)
        private val labelTextView1: TextView = itemView.findViewById(R.id.tvCategory1)
        private val labelTextView2: TextView = itemView.findViewById(R.id.tvCategory2)
        private val labelTextView3: TextView = itemView.findViewById(R.id.tvCategory3)
        private val colorTextView1: TextView = itemView.findViewById(R.id.tvColor1)
        private val colorTextView2: TextView = itemView.findViewById(R.id.tvColor2)
        private val colorTextView3: TextView = itemView.findViewById(R.id.tvColor3)

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

            colorTextView1.text = outfit.outer.color
            colorTextView2.text = outfit.top.color
            colorTextView3.text = outfit.bottom.color
        }

        init {
            // Adjust the layout params of the item view to make it occupy the full width
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
