package com.dicoding.wearit.ui.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.wearit.databinding.FragmentRecommendationBinding

class RecommendationFragment : Fragment() {

    private var _binding: FragmentRecommendationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recommendationViewModel =
            ViewModelProvider(this).get(RecommendationViewModel::class.java)

        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewPager: ViewPager = binding.viewpager
        val textList = ArrayList<String>()
        textList.add("Slide 1")
        textList.add("Slide 2")
        textList.add("Slide 3")

        val mViewPagerAdapter = ViewPagerAdapter(requireContext(), textList)

        viewPager.apply {
            adapter = mViewPagerAdapter
            setPageTransformer(ViewPager2.PageTransformer { page, position ->
                // Apply desired transformation to the page
            })
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
