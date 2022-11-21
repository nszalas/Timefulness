package com.nszalas.timefulness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       /* val bottomMenuNavigation = findViewById<BottomNavigationView>(R.id.bottomMenuView)
        val navController = findNavController(R.id.fragmentContainerView)

        bottomMenuNavigation.setupWithNavController(navController)*/


    }
}