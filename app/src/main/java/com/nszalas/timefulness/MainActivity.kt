package com.nszalas.timefulness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nszalas.timefulness.databinding.ActivityMainBinding
import com.nszalas.timefulness.ui.start.FragmentStartDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            binding.bottomNavigation.isVisible = when (destination.id) {
                R.id.fragmentSignIn,
                R.id.fragmentSignUp,
                R.id.fragmentStart,
                R.id.addTaskFragment -> {
                    false
                }
                else -> {
                    true
                }
            }

            if (destination.id == R.id.fragmentStart && viewModel.isLoggedIn()) {
                controller.navigate(
                    FragmentStartDirections.actionFragmentStartToNavigationProfile()
                )
            }
        }

        binding.bottomNavigation.setupWithNavController(navController)
    }
}