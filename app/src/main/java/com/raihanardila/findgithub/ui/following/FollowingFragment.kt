package com.raihanardila.findgithub.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanardila.findgithub.R
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.databinding.FragmentFollowingBinding
import com.raihanardila.findgithub.ui.adapter.UserAdapter
import com.raihanardila.findgithub.ui.base.HomeDetailFragment
import com.raihanardila.findgithub.ui.interfaces.OnLoveButtonClickListener
import com.raihanardila.findgithub.ui.viewmodel.FollowingViewModel
import com.raihanardila.findgithub.ui.viewmodel.HomeViewModel

class FollowingFragment : Fragment(), OnLoveButtonClickListener {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var userViewModel: HomeViewModel
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
        userViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        val args = arguments
        username = args?.getString(HomeDetailFragment.EXTRA_USERNAME).toString()

        adapter = UserAdapter(this)
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

    override fun onLoveButtonClick(position: Int) {
        val user = adapter.getItem(position)
        val isFavorite = user?.isFavorite ?: return
        val username = user.login

        if (isFavorite) {
            userViewModel.deleteFavoriteUser(user)
            Toast.makeText(requireContext(), "$username Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            userViewModel.insertFavoriteUser(user)
            Toast.makeText(requireContext(), "$username Added to favorites", Toast.LENGTH_SHORT).show()
        }

        user.isFavorite = !isFavorite // Toggle status favorite

        // Update tampilan tombol love berdasarkan status favorit yang baru
        val buttonLove = binding.recyclerViewFollowing.findViewHolderForAdapterPosition(position)?.itemView?.findViewById<ImageButton>(R.id.buttonLove)
        if (buttonLove != null) {
            buttonLove.setImageResource(
                if (user.isFavorite) R.drawable.ic_fv_clarity_solid
                else R.drawable.ic_fv_clarity
            )
        }
    }
}

