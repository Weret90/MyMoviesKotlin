package com.umbrella.mymovieskotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import com.umbrella.mymovieskotlin.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_main) {
            findNavController(R.id.fragmentContainerView).navigate(R.id.filmsFragment)
        }
        if (item.itemId == R.id.item_favourite) {
            findNavController(R.id.fragmentContainerView).navigate(R.id.favouriteFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}