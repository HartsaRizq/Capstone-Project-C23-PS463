package com.dicoding.wearit.ui.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.wearit.database.Image
import com.dicoding.wearit.database.ImageDao
import com.dicoding.wearit.database.ImagesDatabase
import com.dicoding.wearit.database.Outfit
import com.dicoding.wearit.R
import com.dicoding.wearit.database.FavoriteOutfitDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random

class RecommendationFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageDao: ImageDao
    private lateinit var favoriteOutfitDao: FavoriteOutfitDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recommendation, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imagesDatabase = ImagesDatabase.getInstance(requireContext().applicationContext)
        imageDao = imagesDatabase.imageDao()
        favoriteOutfitDao = imagesDatabase.favoriteOutfitDao()

        imageAdapter = ImageAdapter()
        imageAdapter.setFavoriteOutfitDao(favoriteOutfitDao)

        lifecycleScope.launch {
            val images: List<Image> = withContext(Dispatchers.IO) {
                imageDao.getAllImages()
            }

            val outfits = generateOutfits(images)
            imageAdapter.setData(outfits)
            recyclerView.adapter = imageAdapter

        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = imageAdapter
        }
    }
    private fun generateOutfits(images: List<Image>): List<Outfit> {
        val outerImages = images.filter { it.predictedLabel in listOf("Blazzer", "Coat", "Sweter", "Gym_Jacket", "Hoodie", "Denim_Jacket", "Jacket") }
        val topImages = images.filter { it.predictedLabel in listOf("Dress", "Shirt (Kemeja)", "Polo", "T-Shirt") }
        val bottomImages = images.filter { it.predictedLabel in listOf("Pants", "Skirt", "Shorts", "Jeans") }

        val outfits = mutableListOf<Outfit>()
        val random = Random()

        for (i in 0 until minOf(outerImages.size, topImages.size, bottomImages.size)) {
            val outer = outerImages[random.nextInt(outerImages.size)]
            val top = topImages[random.nextInt(topImages.size)]
            val bottom = bottomImages[random.nextInt(bottomImages.size)]
            outfits.add(Outfit(outer, top, bottom))
        }

        return outfits
    }


}