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
    private var imageList: List<Image> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_card, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]

        // Create a Uri from the image path
        val uri = Uri.parse(image.imagePath)

        Glide.with(holder.itemView)
            .load(uri)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.imageView)

        holder.categoryTextView.text = image.predictedLabel
        holder.colorTextView.text = image.color
    }



    override fun getItemCount(): Int {
        return imageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateImageList(newImageList: List<Image>) {
        imageList = newImageList
        notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.slide_screen_item_iv2)
        val categoryTextView: TextView = itemView.findViewById(R.id.image1_tv)
        val colorTextView: TextView = itemView.findViewById(R.id.image1_tv2)
    }

}
