package com.dicoding.wearit.ui.recommendation

// In RecommendationFragment
import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.wearit.Database.Image
import com.dicoding.wearit.R

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var imageList: List<Pair<Image, String>> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_card, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val (image, color) = imageList[position]

        val uri = Uri.parse(image.imagePath)
        Glide.with(holder.itemView)
            .load(uri)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.imageViewList[position])

        holder.categoryTextViewList[position].text = image.predictedLabel
        holder.colorTextViewList[position].text = color
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateImageList(newImageList: List<Pair<Image, String>>) {
        imageList = newImageList
        notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewList: List<ImageView> = listOf(
            itemView.findViewById(R.id.slide_screen_item_iv1),
            itemView.findViewById(R.id.slide_screen_item_iv2),
            itemView.findViewById(R.id.slide_screen_item_iv3)
            // Add more ImageView references here for additional image views
        )
        val categoryTextViewList: List<TextView> = listOf(
            itemView.findViewById(R.id.image1_tv),
            itemView.findViewById(R.id.image2_tv),
            itemView.findViewById(R.id.image3_tv)
            // Add more TextView references here for additional image views
        )
        val colorTextViewList: List<TextView> = listOf(
            itemView.findViewById(R.id.image1_tv2),
            itemView.findViewById(R.id.image2_tv2),
            itemView.findViewById(R.id.image3_tv2)
            // Add more TextView references here for additional image views
        )
    }
}

