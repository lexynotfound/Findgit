package com.raihanardila.findgithub.ui.profile

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.raihanardila.findgithub.databinding.FragmentProfileBinding
import com.raihanardila.findgithub.ui.viewmodel.FollowersViewModel
import com.raihanardila.findgithub.ui.viewmodel.FollowingViewModel
import com.raihanardila.findgithub.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi viewModel
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        // Tampilkan ProgressBar saat memuat data
        binding.progressBar.visibility = View.VISIBLE

        // Panggil method fetchUser dan fetchReadme menggunakan lifecycleScope
        lifecycleScope.launch {
            viewModel.fetchUser()
            viewModel.fetchReadme()
            viewModel.getUserFollowers()
            viewModel.getUserFollowing()
        }

        // Observasi data pengguna (user)
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            Log.d("ProfileFragment", "Username: ${user.login}")
            Log.d("ProfileFragment", "Name: ${user.name}")
            Log.d("ProfileFragment", "Followers: ${user.followers}")
            Log.d("ProfileFragment", "Following: ${user.following}")
            // Update tampilan dengan data pengguna (user) yang diterima
            binding.usernameTextView.text = user.login
            binding.nameTextView.text = user.name
            // Tampilkan jumlah followers dan following
            binding.followersCount.text = user.followers
            binding.followingCount.text = user.following
            // Sembunyikan ProgressBar setelah data selesai dimuat

            binding.progressBar.visibility = View.GONE
            // Load gambar profil menggunakan Glide
            Glide.with(this@ProfileFragment)
                .load(user.avatarURL)
                .transform(CircleCrop())
                .into(binding.profileImage)



        })

        // Observasi data pengikut (followers)
        viewModel.userFollowers.observe(viewLifecycleOwner, Observer { followers ->
            // Load gambar pengikut (followers) pertama
            if (followers.isNotEmpty()) {
                Glide.with(this@ProfileFragment)
                    .load(followers[0].avatarURL)
                    .transform(CircleCrop())
                    .into(binding.followersIcon)
            }
        })

        // Observasi data mengikuti (following)
        viewModel.userFollowing.observe(viewLifecycleOwner, Observer { following ->
            // Load gambar pengikut (followers) pertama
            if (following.isNotEmpty()) {
                Glide.with(this@ProfileFragment)
                    .load(following[0].avatarURL)
                    .transform(CircleCrop())
                    .into(binding.followingIcon)
            }
        })

        // Observasi data README
        viewModel.readme.observe(viewLifecycleOwner, Observer { readme ->
            Log.d("ProfileFragment", "Readme Content: ${readme.content}")
            // Decode konten README
            val decodedContent = decodeBase64(readme.content)
            // Format konten README sesuai keinginan
            val formattedContent = decodedContent
                .split("\n")
                .joinToString("\n") { line ->
                    if (line.isNotBlank()) {
                        "• $line"
                    } else {
                        ""
                    }
                }
            // Update tampilan dengan konten README yang sudah diformat
            binding.bioTextView.text = formattedContent
        })
    }

    // Fungsi untuk mendekode teks dari Base64
    private fun decodeBase64(encodedText: String): String {
        val decodedBytes = Base64.decode(encodedText, Base64.DEFAULT)
        return String(decodedBytes, StandardCharsets.UTF_8)
    }
}



