package com.dicoding.wearit.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.wearit.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        setupRecyclerView()

        favoriteViewModel.favoriteItems.observe(viewLifecycleOwner, Observer { items ->
            favoriteAdapter.setItems(items)
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter()

        binding.favoriteRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favoriteAdapter
        }
    }
}
