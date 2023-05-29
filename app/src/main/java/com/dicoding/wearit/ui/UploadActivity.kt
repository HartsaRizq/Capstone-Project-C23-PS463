package com.dicoding.wearit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.wearit.Clothes
import com.dicoding.wearit.R
import com.dicoding.wearit.databinding.ActivityUploadBinding
import com.dicoding.wearit.ui.favorite.InnerwearFragment
import com.dicoding.wearit.ui.favorite.OuterwearFragment
import com.dicoding.wearit.ui.favorite.PantsFragment

class UploadActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUploadBinding
    private val list = ArrayList<Clothes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvOuterwear.setHasFixedSize(true)
        val btnOuterwear = binding.btnOuterwear
        btnOuterwear.setOnClickListener(this)
        val btnInnerwear = binding.btnInnerwear
        btnInnerwear.setOnClickListener(this)
        val btnPants = binding.btnPants
        btnPants.setOnClickListener(this)

        list.addAll(getListClothes())
        showRecyclerList()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_outerwear -> {
                val outerwearIntent = Intent(this, OuterwearFragment::class.java)
                startActivity(outerwearIntent)
            }
            R.id.btn_innerwear -> {
                val innerwearIntent = Intent(this, InnerwearFragment::class.java)
                startActivity(innerwearIntent)
            }
            R.id.btn_pants -> {
                val pantsIntent = Intent(this, PantsFragment::class.java)
                startActivity(pantsIntent)
            }
        }
    }

    private fun getListClothes(): ArrayList<Clothes> {
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listClothes = ArrayList<Clothes>()
        for (i in 0 until dataPhoto.length()) {
            val hero = Clothes(dataPhoto.getResourceId(i, -1))
            listClothes.add(hero)
        }
        return listClothes
    }

    private fun showRecyclerList() {
        binding.rvOuterwear.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val listClothesAdapter = ListClothesAdapter(list)
        binding.rvOuterwear.adapter = listClothesAdapter
    }
}