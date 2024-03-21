package com.raihanardila.findgithub.ui.favorite

import FavoriteUserAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanardila.findgithub.databinding.FragmentFavoriteBinding
import com.raihanardila.findgithub.ui.viewmodel.HomeViewModel
import com.raihanardila.findgithub.ui.base.HomeDetailActivity

class FavoriteFragment : Fragment(), FavoriteUserAdapter.OnFavoriteUserClickListener {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: FavoriteUserAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteViewModel = obtainViewModel(requireContext() as AppCompatActivity)

        adapter = FavoriteUserAdapter(object : FavoriteUserAdapter.OnFavoriteDeleteClickListener {
            override fun onFavoriteDeleteClick(position: Int) {
                // Panggil method untuk menghapus item dari daftar favorit
                favoriteViewModel.deleteFavoriteUsers(adapter.getFavoriteUserAt(position))
                // Hapus item dari adapter setelah dihapus dari daftar favorit
                adapter.removeItem(position)
            }
        }, this)

        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        // Amati LiveData dari ViewModel untuk memperbarui tampilan
        favoriteViewModel.getAllFavorite().observe(viewLifecycleOwner) { favorite ->
            adapter.setListNotes(favorite)
        }
    }

    override fun onFavoriteUserClick(username: String) {
        val intent = Intent(requireContext(), HomeDetailActivity::class.java).apply {
            putExtra(HomeDetailActivity.EXTRA_USERNAME, username)
        }
        startActivity(intent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): HomeViewModel {
        val factory = HomeViewModel.ViewModelFactory(activity.application)
        return ViewModelProvider(activity, factory).get(HomeViewModel::class.java)
    }
}
