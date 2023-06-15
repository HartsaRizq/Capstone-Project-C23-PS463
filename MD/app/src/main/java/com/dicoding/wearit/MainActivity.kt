package com.dicoding.wearit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.wearit.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageIds: LongArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_recommendation
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        imageIds = intent.getLongArrayExtra("imageIds")


        if (imageIds != null && imageIds!!.isNotEmpty()) {
            val bundle = Bundle().apply {
                putLongArray("imageIds", imageIds)
            }
            imageIds = null // Reset the imageIds to empty
            navController.navigate(R.id.navigation_recommendation, bundle)
        }

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    navController.popBackStack(R.id.navigation_home, false)
                    true
                }
                R.id.navigation_favorite -> {
                    navController.navigate(R.id.navigation_favorite)
                    true
                }
                R.id.navigation_recommendation -> {
                    navController.navigate(R.id.navigation_recommendation)
                    true
                }
                else -> false
            }
        }
    }
}

