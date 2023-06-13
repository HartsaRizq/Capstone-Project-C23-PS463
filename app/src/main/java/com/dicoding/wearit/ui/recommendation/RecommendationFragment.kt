package com.dicoding.wearit.ui.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.wearit.Database.Image
import com.dicoding.wearit.Database.ImagesDatabase
import com.dicoding.wearit.R
import kotlinx.coroutines.launch

class RecommendationFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter

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
        imageAdapter = ImageAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = imageAdapter
        }

        loadImagesFromRoom()
    }

    private fun loadImagesFromRoom() {
        lifecycleScope.launch {
            val imageList = getImageListFromRoom()
            imageAdapter.updateImageList(imageList)
        }
    }

    private suspend fun getImageListFromRoom(): List<Image> {
        val database = Room.databaseBuilder(
            requireContext(),
            ImagesDatabase::class.java,
            "images_database"
        ).build()

        val imageDao = database.imageDao()
        return imageDao.getAllImages()
    }
}
