package com.raihanardila.findgithub.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanardila.findgithub.R
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.databinding.ActivitySearchBinding
import com.raihanardila.findgithub.ui.adapter.UserAdapter
import com.raihanardila.findgithub.ui.base.HomeDetailActivity
import com.raihanardila.findgithub.ui.base.HomeDetailFragment
import com.raihanardila.findgithub.ui.interfaces.OnLoveButtonClickListener
import com.raihanardila.findgithub.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.*

class SearchActivity : AppCompatActivity(), UserAdapter.OnUserClickListener,
    OnLoveButtonClickListener {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var userAdapter: UserAdapter

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Menerapkan padding sistem bar menggunakan WindowInsetsCompat
        ViewCompat.setOnApplyWindowInsetsListener(binding.searchMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        userAdapter = UserAdapter(this)

        userAdapter.setOnUserClickListener(this)

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@SearchActivity)
            rvUser.addItemDecoration(
                DividerItemDecoration(
                    this@SearchActivity, DividerItemDecoration.VERTICAL
                )
            )
            rvUser.adapter = userAdapter

            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrBlank()) {
                        performSearch(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Allow search to happen for any text length
                    performSearch(newText.orEmpty())
                    return true
                }
            })
        }
    }


    private fun performSearch(query: String) {
        searchJob?.cancel() // Cancel previous job if any
        searchJob = CoroutineScope(Dispatchers.Main).launch {
            binding.progressBar.visibility = View.VISIBLE
            delay(300) // Debouncing: wait for 300 milliseconds before performing search
            viewModel.setSearchUsers(query)
            observeSearchResults()
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun observeSearchResults() {
        viewModel.searchUsers().observe(this) { users ->
            userAdapter.setUsers(users)
            binding.rvUser.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchJob?.cancel() // Cancel the search job when the activity is destroyed
    }

    override fun onUserClick(data: UsersModel) {
        val intent = Intent(this, HomeDetailActivity::class.java).apply {
            putExtra(HomeDetailFragment.EXTRA_USERNAME, data.login)
        }
        startActivity(intent)
    }

    override fun onLoveButtonClick(position: Int) {
        val user = userAdapter.getItem(position)
        val isFavorite = user?.isFavorite ?: return
        val username = user.login

        if (isFavorite) {
            viewModel.deleteFavoriteUser(user)
            Toast.makeText(this, "$username Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insertFavoriteUser(user)
            Toast.makeText(this, "$username Added to favorites", Toast.LENGTH_SHORT).show()
        }

        user.isFavorite = !isFavorite // Toggle status favorite

        // Update tampilan tombol love berdasarkan status favorit yang baru
        val buttonLove = binding.rvUser.findViewHolderForAdapterPosition(position)?.itemView?.findViewById<ImageButton>(R.id.buttonLove)
        if (buttonLove != null) {
            buttonLove.setImageResource(
                if (user.isFavorite) R.drawable.ic_fv_clarity_solid
                else R.drawable.ic_fv_clarity
            )
        }
    }
}
