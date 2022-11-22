package com.nszalas.timefulness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = this.findNavController(R.id.fragmentContainerView)

        val bottomView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.fragmentSignIn || nd.id == R.id.fragmentSignUp|| nd.id == R.id.fragmentStart) {
                bottomView.visibility = View.GONE
            } else {
                bottomView.visibility = View.VISIBLE
            }
        }

    }
}