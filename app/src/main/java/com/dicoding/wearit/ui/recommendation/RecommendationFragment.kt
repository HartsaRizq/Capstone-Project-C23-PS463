package com.dicoding.wearit.ui.recommendation

import android.os.Bundle
import android.util.Log
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
import com.dicoding.wearit.Database.ImageDao
import com.dicoding.wearit.Database.ImagesDatabase
import com.dicoding.wearit.Database.Outfit
import com.dicoding.wearit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random

class RecommendationFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageDao: ImageDao

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

        val imagesDatabase = ImagesDatabase.getInstance(requireContext().applicationContext)
        imageDao = imagesDatabase.imageDao()

        lifecycleScope.launch {
            val images: List<Image> = withContext(Dispatchers.IO) {
                imageDao.getAllImages()
            }

            val outfits = generateOutfits(images)
            imageAdapter.setData(outfits)
            recyclerView.adapter = imageAdapter

            /*
            Notes
            ==============================================================
            Input for the recommendation system is not used because the
            recommendation system need reworks
            ==============================================================

            val inputArray: Array<Array<String>> = images.map { arrayOf(it.id.toString(), it.color, it.predictedLabel) }.toTypedArray()
            val outputCombinations = generateCombinations(inputArray)

            for (combination in outputCombinations) {
                println(combination)
            } */
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

    /*
    Notes:
    ===============================================================
    Output of the recommendation system from Machine Learning
    doesn't have a separator which make it hard to use in Android
    ================================================================

    fun generateCombinations(inputArray: Array<Array<String>>): List<List<String>> {
        // Determine the clothing types and their categories
        val parisianTops = listOf("Dress", "Shirt (Kemeja)", "Polo")
        val parisianBottoms = listOf("Pants", "Skirt")
        val parisianOuter = listOf("Blazzer", "Coat", "Sweter")

        val parisianInputTop = mutableListOf<List<String>>()
        val parisianInputBottom = mutableListOf<List<String>>()
        val parisianInputOuter = mutableListOf<List<String>>()

        val athleteTops = listOf("T-Shirt")
        val athleteBottoms = listOf("Shorts")
        val athleteOuter = listOf("Coat", "Gym_Jacket", "Hoodie", "Sweter")

        val athleteInputTop = mutableListOf<List<String>>()
        val athleteInputBottom = mutableListOf<List<String>>()
        val athleteInputOuter = mutableListOf<List<String>>()

        val streetTops = listOf("Dress", "Shirt (Kemeja)", "T-Shirt")
        val streetBottoms = listOf("Jeans", "Shorts", "Pants", "Skirt")
        val streetOuter = listOf("Coat", "Denim_Jacket", "Gym_Jacket", "Hoodie", "Jacket", "Sweter")

        val streetInputTop = mutableListOf<List<String>>()
        val streetInputBottom = mutableListOf<List<String>>()
        val streetInputOuter = mutableListOf<List<String>>()

        val businessTops = listOf("Dress", "Shirt (Kemeja)", "Polo")
        val businessBottoms = listOf("Pants")
        val businessOuter = listOf("Blazzer", "Sweter")

        val businessInputTop = mutableListOf<List<String>>()
        val businessInputBottom = mutableListOf<List<String>>()
        val businessInputOuter = mutableListOf<List<String>>()

        val calmTops = listOf("Dress", "Shirt (Kemeja)", "T-Shirt", "Polo")
        val calmBottoms = listOf("Jeans", "Shorts", "Pants", "Skirt")
        val calmOuter = listOf("Gym_Jacket", "Hoodie", "Jacket", "Sweter")

        val calmInputTop = mutableListOf<List<String>>()
        val calmInputBottom = mutableListOf<List<String>>()
        val calmInputOuter = mutableListOf<List<String>>()

        val grungeTops = listOf("Dress", "T-Shirt")
        val grungeBottoms = listOf("Jeans", "Shorts", "Pants")
        val grungeOuter = listOf("Coat", "Denim_Jacket", "Jacket")

        val grungeInputTop = mutableListOf<List<String>>()
        val grungeInputBottom = mutableListOf<List<String>>()
        val grungeInputOuter = mutableListOf<List<String>>()

        // iterate the input to create a classification where it is differentiated with the order of top, bottom , outer.
        // Filter out based on the color that falls to each category and use the defined variable to filter out the clothing_type

        for (item in inputArray) {
            val id = item[0]
            val color = item[1]
            val clothingType = item[2]

            if (color in listOf("Black", "White", "Blue")) {
                if (clothingType in parisianTops) {
                    parisianInputTop.add(item.toList())
                }
                if (clothingType in parisianBottoms) {
                    parisianInputBottom.add(item.toList())
                }
                if (clothingType in parisianOuter) {
                    parisianInputOuter.add(item.toList())
                }
            }

            if (color in listOf("Black", "White", "Red", "Blue")) {
                if (clothingType in athleteTops) {
                    athleteInputTop.add(item.toList())
                }
                if (clothingType in athleteBottoms) {
                    athleteInputBottom.add(item.toList())
                }
                if (clothingType in athleteOuter) {
                    athleteInputOuter.add(item.toList())
                }
            }

            if (color in listOf("Black", "White", "Red", "Orange", "Green", "Blue", "Violet")) {
                if (clothingType in streetTops) {
                    streetInputTop.add(item.toList())
                }
                if (clothingType in streetBottoms) {
                    streetInputBottom.add(item.toList())
                }
                if (clothingType in streetOuter) {
                    streetInputOuter.add(item.toList())
                }
            }

            if (color in listOf("Black", "White", "Blue")) {
                if (clothingType in businessTops) {
                    businessInputTop.add(item.toList())
                }
                if (clothingType in businessBottoms) {
                    businessInputBottom.add(item.toList())
                }
                if (clothingType in businessOuter) {
                    businessInputOuter.add(item.toList())
                }
            }

            if (color in listOf("Black", "White", "Red", "Orange", "Yellow", "Green", "Blue", "Violet")) {
                if (clothingType in calmTops) {
                    calmInputTop.add(item.toList())
                }
                if (clothingType in calmBottoms) {
                    calmInputBottom.add(item.toList())
                }
                if (clothingType in calmOuter) {
                    calmInputOuter.add(item.toList())
                }
            }

            if (color in listOf("Black", "White", "Red", "Blue")) {
                if (clothingType in grungeTops) {
                    grungeInputTop.add(item.toList())
                }
                if (clothingType in grungeBottoms) {
                    grungeInputBottom.add(item.toList())
                }
                if (clothingType in grungeOuter) {
                    grungeInputOuter.add(item.toList())
                }
            }
        }

        // Creating a list to save the combinations (outfit)
        val parisianCombinations = mutableListOf<List<String>>()
        val parisianCombinationsOuter = mutableListOf<List<String>>()

        val athleteCombinations = mutableListOf<List<String>>()
        val athleteCombinationsOuter = mutableListOf<List<String>>()

        val streetCombinations = mutableListOf<List<String>>()
        val streetCombinationsOuter = mutableListOf<List<String>>()

        val businessCombinations = mutableListOf<List<String>>()
        val businessCombinationsOuter = mutableListOf<List<String>>()

        val calmCombinations = mutableListOf<List<String>>()
        val calmCombinationsOuter = mutableListOf<List<String>>()

        val grungeCombinations = mutableListOf<List<String>>()
        val grungeCombinationsOuter = mutableListOf<List<String>>()

        for (top in parisianInputTop) {
            for (bottom in parisianInputBottom) {
                parisianCombinations.add(top + bottom)
            }
        }

        for (comb in parisianCombinations) {
            for (outer in parisianInputOuter) {
                parisianCombinationsOuter.add(comb + outer)
            }
        }

        for (topa in athleteInputTop) {
            for (bottoma in athleteInputBottom) {
                athleteCombinations.add(topa + bottoma)
            }
        }

        for (comba in athleteCombinations) {
            for (outera in athleteInputOuter) {
                athleteCombinationsOuter.add(comba + outera)
            }
        }

        for (tops in streetInputTop) {
            for (bottoms in streetInputBottom) {
                streetCombinations.add(tops + bottoms)
            }
        }

        for (combs in streetCombinations) {
            for (outers in streetInputOuter) {
                streetCombinationsOuter.add(combs + outers)
            }
        }

        for (topb in businessInputTop) {
            for (bottomb in businessInputBottom) {
                businessCombinations.add(topb + bottomb)
            }
        }

        for (combb in businessCombinations) {
            for (outerb in businessInputOuter) {
                businessCombinationsOuter.add(combb + outerb)
            }
        }

        for (topc in calmInputTop) {
            for (bottomc in calmInputBottom) {
                calmCombinations.add(topc + bottomc)
            }
        }

        for (combc in calmCombinations) {
            for (outerc in calmInputOuter) {
                calmCombinationsOuter.add(combc + outerc)
            }
        }

        for (topg in grungeInputTop) {
            for (bottomg in grungeInputBottom) {
                grungeCombinations.add(topg + bottomg)
            }
        }

        for (combg in grungeCombinations) {
            for (outerg in grungeInputOuter) {
                grungeCombinationsOuter.add(combg + outerg)
            }
        }

        return deleteDuplicates(
            parisianCombinations, parisianCombinationsOuter,
            athleteCombinations, athleteCombinationsOuter,
            streetCombinations, streetCombinationsOuter,
            businessCombinations, businessCombinationsOuter,
            calmCombinations, calmCombinationsOuter,
            grungeCombinations, grungeCombinationsOuter
        )
    }

    fun deleteDuplicates(vararg arrays: MutableList<List<String>>): List<List<String>> {
        val uniqueOutfits = mutableListOf<List<String>>()
        val duplicates = mutableListOf<MutableList<List<String>>>()

        for (array in arrays) {
            if (!duplicates.any { it == array }) {
                duplicates.add(array)
                uniqueOutfits.add(array.flatten())
            }
        }
        return uniqueOutfits
    } */

}