package com.example.dicodingstory.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dicodingstory.data.model.Story
import com.example.dicodingstory.databinding.ActivityDetailBinding
import com.example.dicodingstory.ui.home.HomeFragment.Companion.EXTRA_ID
import com.example.dicodingstory.utils.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val factory = ViewModelFactory.getInstance(this)
        val detailViewModel: DetailViewModel by viewModels {
            factory
        }
        val id = intent.getStringExtra(EXTRA_ID)
        setContentView(binding.root)

        detailViewModel.apply {
            getToken().observe(this@DetailActivity) { token ->
                if (token != null && id != null) {
                    getDetailStory(token, id).observe(this@DetailActivity) {
                        when (it) {
                            is com.example.dicodingstory.data.Result.Loading -> {
                                showLoading(true)
                            }
                            is com.example.dicodingstory.data.Result.Success -> {
                                showDetail(it.data.story)
                                showLoading(false)
                            }
                            is com.example.dicodingstory.data.Result.Error -> {
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDetail(story: Story) {
        binding.apply {
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            tvDetailDate.text = story.createdAt
            tvDetailLatitude.visibility = if (story.lat == null) View.GONE else {
                tvDetailLatitude.text = story.lat.toString()
                View.VISIBLE
            }
            tvDetailLongitude.visibility = if (story.lon == null) View.GONE else {
                tvDetailLongitude.text = story.lon.toString()
                View.VISIBLE
            }
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .centerCrop()
                .into(ivDetailPhoto)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}