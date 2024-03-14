package com.raihanardila.findgithub.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanardila.findgithub.databinding.FragmentHomeBaseBinding
import com.raihanardila.findgithub.ui.adapter.UserAdapter
import com.raihanardila.findgithub.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeBaseFragment : Fragment() {

    private lateinit var binding: FragmentHomeBaseBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(false)
        setupRecyclerView()

        binding.searchBar.setOnClickListener {
            // Handle search action here
            // Example: searchUser()
        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // Load all users when fragment is created
        lifecycleScope.launch {
            viewModel.getAllUserss()
        }

        observeData()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun observeData() {
        viewModel.searchUsers().observe(viewLifecycleOwner) { users ->
            userAdapter.setUsers(users)
        }
    }



    // Uncomment and implement searchUser() function if needed
    /*
    private fun searchUser() {
        val query = binding.etQuery.text.toString()
        if (query.isNotEmpty()) {
            viewModel.setSearchUsers(query)
        } else {
            Toast.makeText(requireContext(), "Please enter a query", Toast.LENGTH_SHORT).show()
        }
    }
    */
}