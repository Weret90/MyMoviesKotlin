package com.umbrella.mymovieskotlin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.umbrella.mymovieskotlin.R
import com.umbrella.mymovieskotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_main -> findNavController(R.id.fragmentContainerView).navigate(R.id.filmsFragment)
                R.id.item_favourite -> findNavController(R.id.fragmentContainerView).navigate(R.id.favouriteFragment)
            }
            true
        }
    }
}