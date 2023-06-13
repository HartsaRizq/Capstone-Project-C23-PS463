package com.dicoding.wearit.ui.recommendation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.wearit.Database.ImagesDatabase
import com.dicoding.wearit.R
import kotlinx.coroutines.launch

class RecommendationFragment : Fragment() {
    // Declare the ViewPager and adapter variables
    private lateinit var viewPager: ViewPager2
    private lateinit var imageAdapter: ImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewPager and adapter
        viewPager = view.findViewById(R.id.viewpager)
        imageAdapter = ImageAdapter()

        // Set the adapter to the ViewPager
        viewPager.adapter = imageAdapter

        // Call a method to load and display the images from Room
        loadImagesFromRoom()
    }

    private fun loadImagesFromRoom() {
        // Use the lifecycleScope.launch extension function to launch a coroutine
        lifecycleScope.launch {
            // Retrieve the image list from Room using a suspend function
            val imageList = getImageListFromRoom()

            // Update the adapter with the new image list
            imageAdapter.updateImageList(imageList)
        }
    }

    private suspend fun getImageListFromRoom(): List<com.dicoding.wearit.Database.Image> {
        // Get an instance of your Room database
        val database = Room.databaseBuilder(
            requireContext(),
            ImagesDatabase::class.java,
            "images_database"
        ).build()

        // Access the image DAO from the database
        val imageDao = database.imageDao()

        // Retrieve the image list using the DAO method
        return imageDao.getAllImages()
    }

}
