package com.raihanardila.findgithub.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayoutMediator
import com.raihanardila.findgithub.MainActivity
import com.raihanardila.findgithub.R
import com.raihanardila.findgithub.databinding.ActivityHomeDetailBinding
import com.raihanardila.findgithub.ui.adapter.ProfilePagerAdapter
import com.raihanardila.findgithub.ui.viewmodel.FollowersViewModel
import com.raihanardila.findgithub.ui.viewmodel.FollowingViewModel
import com.raihanardila.findgithub.ui.viewmodel.HomeDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class HomeDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_REPO = "extra_repo"
        const val EXTRA_OWNER = "extra_owner"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers, R.string.following
        )
    }

    private lateinit var binding: ActivityHomeDetailBinding
    private lateinit var viewModel: HomeDetailViewModel
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BackButton.setOnClickListener{
            //Create an Intent to Start HomeFragment
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        viewModel = ViewModelProvider(this).get(HomeDetailViewModel::class.java)
        followersViewModel = ViewModelProvider(this).get(FollowersViewModel::class.java)
        followingViewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)

        showLoading(true)

        val username = intent.getStringExtra(EXTRA_USERNAME)


        lifecycleScope.launch {
            fetchData(username)
        }

        // Initialize ViewPager
        val bundle = Bundle().apply {
            putString(EXTRA_USERNAME, username)
        }
        val profilePagerAdapter = ProfilePagerAdapter(this, bundle)
        viewPager = binding.viewPager
        viewPager.adapter = profilePagerAdapter

        TabLayoutMediator(binding.tabsLayout, viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()

        observeUserData()
    }

    private suspend fun fetchData(username: String?) {
        username?.let {
            viewModel.fetchUser(it)
            followersViewModel.getUserFollowers(it)
            followingViewModel.getUserFollowing(it)
            viewModel.fetchReadme(it, it)
        }
    }

    private fun observeUserData() {
        viewModel.user.observe(this, Observer { user ->
            binding.apply {
                usernameTextView.text = user.login
                nameTextView.text = user.name
                followersCount.text = user.followers
                followingCount.text = user.following
                binding.ShareButton.setOnClickListener {
                    shareProfile(user.login)
                }
                Glide.with(this@HomeDetailActivity).load(user.avatarURL).transform(CircleCrop())
                    .into(profileImage)
            }
            showLoading(false)


        })

        followersViewModel.userFollowers.observe(this, Observer { followers ->
            if (followers.isNotEmpty()) {
                Glide.with(this@HomeDetailActivity).load(followers[0].avatarURL)
                    .transform(CircleCrop()).into(binding.followersIcon)
            }
        })

        followingViewModel.userFollowing.observe(this, Observer { following ->
            if (following.isNotEmpty()) {
                Glide.with(this@HomeDetailActivity).load(following[0].avatarURL)
                    .transform(CircleCrop()).into(binding.followingIcon)
            }
        })

        viewModel.readme.observe(this, Observer { readme ->
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
            // Update tampilan dengan konten README yang sudah diformat
            binding.bioTextView.text = formattedContent
            displayReadmeContent(decodedContent)
        })
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun displayReadmeContent(readmeContent: String) {
        val imageUrls = extractImageUrls(readmeContent)

        // Clear any existing views in the image container
        binding.imageContainer.removeAllViews()

        // Loop through each image URL and create ImageView for each
        imageUrls.forEach { imageUrl ->
            val imageView = ImageView(this@HomeDetailActivity).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // Load image using Glide into the ImageView
            Glide.with(this@HomeDetailActivity).load(imageUrl).into(imageView)

            // Add the ImageView to the image container
            binding.imageContainer.addView(imageView)
        }
    }

    private fun decodeBase64(encodedText: String): String {
        val decodedBytes = Base64.decode(encodedText, Base64.DEFAULT)
        return String(decodedBytes, StandardCharsets.UTF_8)
    }

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

    private fun extractImageUrls(readmeContent: String): List<String> {
        val imageUrlRegex = "(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)".toRegex()
        return imageUrlRegex.findAll(readmeContent).map { it.value }.toList()
    }
}
