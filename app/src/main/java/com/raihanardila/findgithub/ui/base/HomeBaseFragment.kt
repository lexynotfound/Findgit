package com.raihanardila.findgithub.ui.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanardila.findgithub.R
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.databinding.FragmentHomeBaseBinding
import com.raihanardila.findgithub.ui.adapter.UserAdapter
import com.raihanardila.findgithub.ui.interfaces.OnLoveButtonClickListener
import com.raihanardila.findgithub.ui.search.SearchActivity
import com.raihanardila.findgithub.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeBaseFragment : Fragment(), UserAdapter.OnUserClickListener, OnLoveButtonClickListener {

    private lateinit var binding: FragmentHomeBaseBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        setupRecyclerView()

        binding.searchBar.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        userAdapter = UserAdapter(this)
        userAdapter.setOnUserClickListener(this)
        binding.rvUser.adapter = userAdapter

        lifecycleScope.launch {
            binding.progressBar.visibility = View.GONE
            viewModel.getAllUserss()
        }

        observeData()
    }

    private fun setupRecyclerView() {
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun observeData() {
        viewModel.searchUsers().observe(viewLifecycleOwner) { users ->
            userAdapter.setUsers(users)
        }
    }

    override fun onUserClick(data: UsersModel) {
        val fragment = HomeDetailFragment()
        val bundle = Bundle()
        bundle.putString(HomeDetailFragment.EXTRA_USERNAME, data.login)
        fragment.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onLoveButtonClick(position: Int) {
        val user = userAdapter.getItem(position)
        val isFavorite = user?.isFavorite ?: return
        val username = user.login

        if (isFavorite) {
            viewModel.deleteFavoriteUser(user)
            Toast.makeText(requireContext(), "$username Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insertFavoriteUser(user)
            Toast.makeText(requireContext(), "$username Added to favorites", Toast.LENGTH_SHORT).show()
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
