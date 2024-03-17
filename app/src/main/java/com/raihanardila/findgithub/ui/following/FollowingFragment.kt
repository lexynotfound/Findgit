package com.raihanardila.findgithub.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.databinding.FragmentFollowingBinding
import com.raihanardila.findgithub.ui.adapter.UserAdapter
import com.raihanardila.findgithub.ui.base.HomeDetailFragment
import com.raihanardila.findgithub.ui.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(HomeDetailFragment.EXTRA_USERNAME).toString()

        adapter = UserAdapter()
        adapter.setOnUserClickListener(object : UserAdapter.OnUserClickListener {
            override fun onUserClick(user: UsersModel) {
                // Handle user click action here
            }
        })

        binding.recyclerViewFollowing.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FollowingFragment.adapter
        }

        viewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)
        viewModel.userFollowing.observe(viewLifecycleOwner) { users ->
            adapter.setUsers(users)
        }

        viewModel.getUserFollowing(username)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

