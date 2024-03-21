package com.raihanardila.findgithub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.raihanardila.findgithub.databinding.ActivityMainBinding
import com.raihanardila.findgithub.ui.base.HomeBaseFragment
import com.raihanardila.findgithub.ui.favorite.FavoriteFragment
import com.raihanardila.findgithub.ui.profile.ProfileFragment
import com.raihanardila.findgithub.ui.search.SearchFragment
import com.raihanardila.findgithub.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        hideActionBar()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    hideActionBar()
                    loadFragment(HomeBaseFragment())
                    true
                }

                R.id.search -> {
                    hideActionBar()
                    loadFragment(SearchFragment())
                    true
                }

                R.id.fav -> {
                    hideActionBar()
                    loadFragment(FavoriteFragment())
                    true
                }

                R.id.profile -> {
                    hideActionBar()
                    loadFragment(ProfileFragment())
                    true
                }

                else -> {
                    false
                }

            }
        }
        loadFragment(HomeBaseFragment())
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }

    private fun showActionBar() {
        supportActionBar?.show()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}