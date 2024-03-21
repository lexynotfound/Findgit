package com.raihanardila.findgithub.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayoutMediator
import com.raihanardila.findgithub.R
import com.raihanardila.findgithub.databinding.FragmentProfileBinding
import com.raihanardila.findgithub.ui.adapter.ProfilePagerAdapter
import com.raihanardila.findgithub.ui.settings.SettingsActivity
import com.raihanardila.findgithub.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            viewModel.fetchUser()
            viewModel.fetchReadme()
            viewModel.getUserFollowers()
            viewModel.getUserFollowing()
        }

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            binding.usernameTextView.text = user.login
            binding.nameTextView.text = user.name
            binding.followersCount.text = user.followers
            binding.followingCount.text = user.following
            binding.progressBar.visibility = View.GONE

            // Set click listener for share icon
            binding.imageShareView.setOnClickListener {
                shareProfile(user.login)
            }

            Glide.with(this@ProfileFragment)
                .load(user.avatarURL)
                .transform(CircleCrop())
                .into(binding.profileImage)
        })

        viewModel.userFollowers.observe(viewLifecycleOwner, Observer { followers ->
            if (followers.isNotEmpty()) {
                Glide.with(this@ProfileFragment)
                    .load(followers[0].avatarURL)
                    .transform(CircleCrop())
                    .into(binding.followersIcon)
            }
        })

        viewModel.userFollowing.observe(viewLifecycleOwner, Observer { following ->
            if (following.isNotEmpty()) {
                Glide.with(this@ProfileFragment)
                    .load(following[0].avatarURL)
                    .transform(CircleCrop())
                    .into(binding.followingIcon)
            }
        })

        viewModel.readme.observe(viewLifecycleOwner, Observer { readme ->
            val decodedContent = decodeBase64(readme.content)
            val formattedContent = decodedContent
                .split("\n")
                .joinToString("\n") { line ->
                    if (line.isNotBlank()) {
                        "• $line"
                    } else {
                        ""
                    }
                }
            binding.bioTextView.text = formattedContent
        })

        val profilePagerAdapter = ProfilePagerAdapter(requireContext(), Bundle())
        viewPager = binding.viewPager
        viewPager.adapter = profilePagerAdapter

        TabLayoutMediator(binding.tabsLayout, viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()
    }

    private fun decodeBase64(encodedText: String): String {
        val decodedBytes = Base64.decode(encodedText, Base64.DEFAULT)
        return String(decodedBytes, StandardCharsets.UTF_8)
    }

    // Function to share profile
    private fun shareProfile(username: String) {
        // Create URL with username
        val url = "https://www.github.com/$username"

        // Create intent to share link
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_profile_subject))
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)

        // Start activity to share
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_profile)))
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}
