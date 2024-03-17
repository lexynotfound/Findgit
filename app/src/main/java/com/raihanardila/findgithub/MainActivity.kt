package com.raihanardila.findgithub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.raihanardila.findgithub.databinding.ActivityMainBinding
import com.raihanardila.findgithub.ui.base.HomeBaseFragment
import com.raihanardila.findgithub.ui.favorite.FavoriteFragment
import com.raihanardila.findgithub.ui.profile.ProfileFragment
import com.raihanardila.findgithub.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showActionBar()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadFragment(HomeBaseFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    showActionBar()
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
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }

    private fun showActionBar() {
        supportActionBar?.show()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}