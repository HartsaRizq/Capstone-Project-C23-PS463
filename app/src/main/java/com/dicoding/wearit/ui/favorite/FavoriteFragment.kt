package com.dicoding.wearit.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.wearit.database.FavoriteOutfitDao
import com.dicoding.wearit.database.ImagesDatabase
import com.dicoding.wearit.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private lateinit var favoriteOutfitDao: FavoriteOutfitDao
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imagesDatabase = ImagesDatabase.getInstance(requireContext())
        favoriteOutfitDao = imagesDatabase.favoriteOutfitDao()

        favoriteAdapter = FavoriteAdapter(favoriteOutfitDao)
        recyclerView.adapter = favoriteAdapter

        loadFavoriteOutfits()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadFavoriteOutfits() {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteOutfits = favoriteOutfitDao.getAllFavoriteOutfits()
            CoroutineScope(Dispatchers.Main).launch {
                favoriteAdapter.setData(favoriteOutfits)
            }
        }
    }
}
