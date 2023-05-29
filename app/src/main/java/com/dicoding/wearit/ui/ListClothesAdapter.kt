package com.dicoding.wearit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.wearit.Clothes
import com.dicoding.wearit.R

class ListClothesAdapter(private val imageList: ArrayList<Clothes>) : RecyclerView.Adapter<ListClothesAdapter.ImageViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val (photo) = imageList[position]
        holder.imageView.setImageResource(photo)
    }

    override fun getItemCount(): Int = imageList.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_item_photo)
    }

}